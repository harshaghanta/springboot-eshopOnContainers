# Unpack the subnets_config map into distinct lists required by the VPC module
locals {
  public_subnets  = { for k, v in var.subnets_config : k => v if v.type == "public" }
  private_subnets = { for k, v in var.subnets_config : k => v if v.type == "private" }
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 5.0"
  
  name                 = "${var.environment}-eshop-vpc"
  cidr                 = var.vpc_cidr

  # Unpack AZs, CIDRs, and custom subnet Names dynamically

  azs                  = distinct([for subnet in values(var.subnets_config) : subnet.az])
  public_subnets       = [for subnet in values(local.public_subnets) : subnet.cidr]
  private_subnets      = [for subnet in values(local.private_subnets) : subnet.cidr]
  
  public_subnet_names  = [for subnet in values(local.public_subnets) : subnet.name]
  private_subnet_names = [for subnet in values(local.private_subnets) : subnet.name]
  
  enable_nat_gateway   = true
  single_nat_gateway   = true
  enable_dns_hostnames = true
  enable_dns_support   = true

  # Load Balancer auto-discovery tags
  public_subnet_tags = {
    "kubernetes.io/role/elb"                     = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
  }

  private_subnet_tags = {
    "kubernetes.io/role/internal-elb"            = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
  }

  tags = {
    Environment = var.environment
    ManagedBy   = "Terraform"
  }
}