terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    helm = {
      source  = "hashicorp/helm"
      version = ">= 3.0"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.0"
    }
    tls = {
      source  = "hashicorp/tls"
      version = "~> 4.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# Look up current cluster data to bootstrap the providers
data "aws_eks_cluster" "current_cluster" {
  name = var.cluster_name
}

data "aws_eks_cluster_auth" "current_auth" {
  name = var.cluster_name
}

# Bind authentication to the Kubernetes & Helm providers
provider "kubernetes" {
  host                   = data.aws_eks_cluster.current_cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.current_cluster.certificate_authority[0].data)
  token                  = data.aws_eks_cluster_auth.current_auth.token
}

provider "helm" {
  kubernetes = {
    host                   = data.aws_eks_cluster.current_cluster.endpoint
    cluster_ca_certificate = base64decode(data.aws_eks_cluster.current_cluster.certificate_authority[0].data)
    token                  = data.aws_eks_cluster_auth.current_auth.token
  }
}

# Run the Vault configuration
module "vault_deployment" {
  source       = "./modules/vault"
  environment  = var.environment
  aws_region   = var.aws_region
  cluster_name = var.cluster_name
}

# Global Variables
variable "aws_region" {
  type    = string
  default = "ap-south-2"
}

variable "cluster_name" {
  type = string
}

variable "environment" {
  type    = string
  default = "prod"
}   