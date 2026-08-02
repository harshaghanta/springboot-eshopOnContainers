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
  name          =  var.eks_encryption_key_alias
  target_key_id = aws_kms_key.eks_shared.key_id
}

resource "aws_kms_key" "vault" {
  description             = "KMS Key for Vault Auto-Unseal"
  deletion_window_in_days = 10
  enable_key_rotation     = true

  tags = {
    Environment = var.environment
    ManagedBy   = "Terraform-Prerequisites"
  }
}

resource "aws_kms_alias" "vault" {
  name          = var.vault_unseal_kms_alias
  target_key_id = aws_kms_key.vault.key_id
}

