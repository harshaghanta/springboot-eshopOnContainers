# ------------------------------------------------------------------------------
# SHARED KMS KEY FOR EKS CLUSTER & WORKLOADS
# ------------------------------------------------------------------------------
resource "aws_kms_key" "eks_shared" {
  description             = "Shared KMS Key for EKS cluster secrets and node volume encryption"
  deletion_window_in_days = 30
  enable_key_rotation     = true

  tags = {
    Environment = var.environment
    ManagedBy   = "Terraform-Prerequisites"
  }
}

resource "aws_kms_alias" "eks_shared_alias" {
  name          = "alias/eks/eshop-eks-cluster"
  target_key_id = aws_kms_key.eks_shared.key_id
}