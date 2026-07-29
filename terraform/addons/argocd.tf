resource "kubernetes_namespace_v1" "argocd" {
  metadata {
    name = "argocd"
  }
}

resource "helm_release" "argocd" {
  name             = "argocd"
  repository       = "https://argoproj.github.io/argo-helm"
  chart            = "argo-cd"
  version          = "6.7.18"
  namespace        = kubernetes_namespace_v1.argocd.metadata[0].name
  create_namespace = false

  values = [
    yamlencode({
      global = {
        nodeSelector = {
          "category" = "platform"
        }
        tolerations = [
          {
            key      = "category"
            operator = "Equal"
            value    = "platform"
            effect   = "NoSchedule"
          }
        ]
      },
      configs = {
        params = {
          "server.insecure" = "true"
        }
      }
    })
  ]
}