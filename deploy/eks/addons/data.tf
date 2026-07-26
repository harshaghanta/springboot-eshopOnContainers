# Fetch current AWS Account ID (used for IRSA OIDC string)
data "aws_caller_identity" "current" {}

# Query EKS cluster details directly from AWS API
data "aws_eks_cluster" "this" {
  name = var.cluster_name
}

data "aws_eks_cluster_auth" "this" {
  name = var.cluster_name
}