variable "environment" {
  type        = string
  description = "Deployment layer (dev/prod)"
}

variable "aws_region" {
  type        = string
  description = "AWS region for KMS and Helm"
}

variable "cluster_name" {
  type        = string
  description = "The target EKS cluster name"
}

variable "vault_namespace" {
  type        = string
  default     = "vault"
}