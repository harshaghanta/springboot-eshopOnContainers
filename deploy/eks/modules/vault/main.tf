# 1. Look up live EKS cluster details
data "aws_eks_cluster" "eks" {
  name = var.cluster_name
}

data "tls_certificate" "eks_oidc" {
  url = data.aws_eks_cluster.eks.identity[0].oidc[0].issuer
}

resource "aws_iam_openid_connect_provider" "eks" {
  url             = data.aws_eks_cluster.eks.identity[0].oidc[0].issuer
  client_id_list  = ["sts.amazonaws.com"]
  thumbprint_list = [data.tls_certificate.eks_oidc.certificates[0].sha1_fingerprint]
}

# 2. Provision the AWS KMS Symmetric Key for Auto-Unseal
resource "aws_kms_key" "vault_auto_unseal" {
  description             = "Symmetric key for Vault Auto-Unseal in cluster: ${var.cluster_name}"
  deletion_window_in_days = 7
  enable_key_rotation = true

  tags = {
    Component   = "Security"
    Environment = var.environment
  }
}

# 3. Create the KMS Access Policy
resource "aws_iam_policy" "vault_kms_permissions" {
    name       = "${var.cluster_name}-vault-kms-policy"
    description = "Allows Vault pods to use the symmetric KMS key for unsealing"

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
                Resource = aws_kms_key.vault_auto_unseal.arn
            }
        ]
    })
}

# 4. Use official AWS IRSA helper to bind OIDC dynamically
module "vault_irsa" {
  source  = "aws-ia/eks-blueprints-addon/aws"
  version = "~> 1.0"

  create_release = false  
  create_role = true
  role_name   = "${var.cluster_name}-vault-kms-role"

  oidc_providers = {
    main = {
      provider_arn    = aws_iam_openid_connect_provider.eks.arn
      namespace       = var.vault_namespace
      service_account = "vault"
    }
  }

  role_policies = {
    kms_access = aws_iam_policy.vault_kms_permissions.arn
  }
}

# 5. Deploy Vault via Helm
resource "helm_release" "vault" {
  name       = "vault"
  repository = "https://helm.releases.hashicorp.com"
  chart      = "vault"
  namespace  = var.vault_namespace
  create_namespace = true
  force_update     = true

  values = [
    yamlencode({
      global = {
        enabled    = true
        tlsDisable = true
      }
      server = {
        extraEnvironmentVars = {
          VAULT_ADDR = "http://127.0.0.1:8200"
        }
        ha = {
          enabled  = true
          replicas = 3
          raft = {
            enabled   = true
            setNodeId = true
            config    = <<-EOT
              ui = true
              listener "tcp" {
                tls_disable = 1
                address     = "[::]:8200"
                cluster_address = "[::]:8201"
              }
              storage "raft" { path = "/vault/data" }
              seal "awskms" {
                region     = "${var.aws_region}"
                kms_key_id = "${aws_kms_key.vault_auto_unseal.key_id}"
              }
            EOT
          }
        }
        serviceAccount = {
          create      = true
          name        = "vault"
          annotations = { "eks.amazonaws.com/role-arn" = module.vault_irsa.iam_role_arn }
        }
        dataStorage = { enabled = true, size = "3Gi", storageClass = "gp2" }
      }
    })
  ]
}