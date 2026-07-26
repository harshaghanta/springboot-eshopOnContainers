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
              }

              service_registration "kubernetes" {}
            EOT
          }
        }
      }
    })
  ]
}