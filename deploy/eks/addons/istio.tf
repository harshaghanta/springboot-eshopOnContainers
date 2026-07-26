resource "kubernetes_namespace_v1" "istio_system" {
  metadata {
    name = "istio-system"
  }
}

# 1. Istio Base CRDs
resource "helm_release" "istio_base" {
  name             = "istio-base"
  repository       = "https://istio-release.storage.googleapis.com/charts"
  chart            = "base"
  version          = "1.20.3"
  namespace        = kubernetes_namespace_v1.istio_system.metadata[0].name
  create_namespace = false
}

# 2. Istiod Control Plane
resource "helm_release" "istiod" {
  name             = "istiod"
  repository       = "https://istio-release.storage.googleapis.com/charts"
  chart            = "istiod"
  version          = "1.20.3"
  namespace        = kubernetes_namespace_v1.istio_system.metadata[0].name
  wait             = true
  timeout          = 300

  values = [
    yamlencode({
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
    })
  ]

  depends_on = [helm_release.istio_base]
}

# 3. Istio Ingress Gateway Namespace
# CRITICAL FIX: Do NOT put istio-injection = "enabled" here!
resource "kubernetes_namespace_v1" "istio_ingress" {
  metadata {
    name = "istio-ingress"
  }
}

# 4. Istio Ingress Gateway
resource "helm_release" "istio_ingress" {
  name             = "istio-ingressgateway"
  repository       = "https://istio-release.storage.googleapis.com/charts"
  chart            = "gateway"
  version          = "1.20.3"
  namespace        = kubernetes_namespace_v1.istio_ingress.metadata[0].name
  create_namespace = false
  wait             = true
  timeout          = 600

  values = [
    yamlencode({
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
    })
  ]

  depends_on = [helm_release.istiod, kubernetes_namespace_v1.istio_ingress]
}