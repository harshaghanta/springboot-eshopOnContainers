# 1. AWS KMS Key for Vault Auto-Unseal
resource "aws_kms_key" "vault" {
  description             = "KMS Key for Vault Auto-Unseal"
  deletion_window_in_days = 10
  enable_key_rotation     = true
}

resource "aws_kms_alias" "vault" {
  name          = "alias/vault-auto-unseal"
  target_key_id = aws_kms_key.vault.key_id
}

# 2. IAM Policy granting KMS Encrypt/Decrypt
resource "aws_iam_policy" "vault_kms" {
  name        = "VaultKMSAutoUnsealPolicy"
  description = "Allows Vault to use KMS for auto-unsealing"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "kms:Encrypt",
          "kms:Decrypt",
          "kms:DescribeKey"
        ]
        Resource = aws_kms_key.vault.arn
      }
    ]
  })
}

# 3. IRSA Role (Attach this to Vault's service account)
module "vault_kms_irsa_role" {
  source  = "terraform-aws-modules/iam/aws//modules/iam-role-for-service-accounts-eks"
  version = "~> 5.0"

  role_name = "vault-auto-unseal-role"

  role_policy_arns = {
    kms_policy = aws_iam_policy.vault_kms.arn
  }

  oidc_providers = {
    main = {
      provider_arn               = data.aws_iam_openid_connect_provider.this.arn
      namespace_service_accounts = ["vault:vault"]
    }
  }
}

resource "helm_release" "vault" {
  name             = "vault"
  repository       = "https://helm.releases.hashicorp.com"
  chart            = "vault"
  version          = "0.27.0"
  namespace        = "vault"
  create_namespace = true

  values = [
    yamlencode({
      server = {
        # Attach the IRSA IAM role to Vault's Service Account
        serviceAccount = {
          annotations = {
            "eks.amazonaws.com/role-arn" = module.vault_kms_irsa_role.iam_role_arn
          }
        }

        nodeSelector = {
          "category" = "platform"
        }
        # tolerations = [
        #   {
        #     key      = "category"
        #     operator = "Equal"
        #     value    = "platform"
        #     effect   = "NoSchedule"
        #   }
        # ]

        # Enable storage persistent volume claims
        dataStorage = {
          enabled   = true
          size      = "10Gi"
          mountPath = "/vault/data"
        }

        volumePermissions = {
          enabled = false
        }

        securityContext: {
            runAsNonRoot = true
            runAsUser    = 100
            runAsGroup   = 1000
            fsGroup      = 1000
        }

        ha = {
          enabled = true
          replicas = 2

          # CRITICAL FIX: Explicitly enable Raft so Helm renders volumeClaimTemplates!
          raft = {
            enabled   = true
            setNodeId = true
            config    = <<EOT
              ui = true

              listener "tcp" {
                tls_disable = 1
                address     = "[::]:8200"
                cluster_address = "[::]:8201"
              }

              storage "raft" {
                path = "/vault/data"

                # Automatically discovers and joins other Vault pods in EKS via AWS/Kubernetes tags
                retry_join {
                  auto_join = "provider=k8s namespace=vault label_selector=\"app.kubernetes.io/name=vault\""
                }
              }

              # AWS KMS Auto-Unseal Configuration
              seal "awskms" {
                region     = "us-east-1" # Update to your AWS region
                kms_key_id = "${aws_kms_key.vault.key_id}"
              }

              service_registration "kubernetes" {}
            EOT
          }
        }
      }
    })
  ]
  depends_on = [module.vault_kms_irsa_role]
}