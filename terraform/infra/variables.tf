# Global Variables
variable "aws_region" {
  description = "AWS Region to deploy the EKS cluster"
  type    = string
  default = "ap-south-2"
}

variable "cluster_name" {
  description = "Name of the EKS cluster"
  type = string
  default = "eshop-eks-cluster"
}

variable "environment" {
  description = "Deployment environment"
  type    = string
  default = "prod"
}

variable "cluster_version" {
  description = "Kubernetes Version for the EKS Cluster"
  type        = string
  default     = "1.30"
}

variable "vpc_cidr" {
  description = "Base IPv4 CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

# Unified data structure holding subnet mapping
variable "subnets_config" {
  description = "Single map containing all public and private subnets, AZs, CIDRs, and names"
  type = map(object({
    type = string # "public" or "private"
    az   = string
    cidr = string
    name = string
  }))
}