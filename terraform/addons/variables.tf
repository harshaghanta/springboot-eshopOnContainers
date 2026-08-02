variable "aws_region" {
  description = "AWS Region where EKS is deployed"
  type        = string
  default     = "ap-south-2"
}

variable "environment" {
  description = "Deployment environment"
  type        = string
  default     = "prod"
}

variable "cluster_name" {
  description = "Name of the target EKS Cluster"
  type        = string
  default     = "eshop-eks-cluster"
}

variable "vault_unseal_kms_alias" {
  description = "KMS Key alias for Vault Auto-Unseal"
  type        = string
  default     = "alias/vault-auto-unseal"
}