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

variable "s3-bucket-name" {
  type        = string
  description = "Name of the S3 bucket for Terraform remote state"
}

variable "kms_alias_name" {
  type        = string
  default     = "alias/eks/eshop-eks-cluster"
  description = "The alias name for the shared EKS KMS Key"
}