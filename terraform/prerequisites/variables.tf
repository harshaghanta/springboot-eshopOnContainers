variable "region" {
  type        = string
  default     = "ap-south-2"
  description = "AWS region for provisioning prerequisites"
}

variable "environment" {
  type        = string
  default     = "prod"
  description = "Deployment environment (e.g., dev, prod)"
}

variable "eks_encryption_key_alias" {
  type        = string
  default     = "alias/eks/eshop-eks-cluster"
  description = "The alias name for the shared EKS KMS Key"
}

variable "vault_unseal_kms_alias" {
  type        = string
  default     = "alias/vault-auto-unseal"
  description = "KMS Key alias for Vault Auto-Unseal"
}

variable "s3_bucket_name" {
  type        = string
  description = "Name of the S3 bucket for Terraform remote state"
}

