aws_region = "ap-south-2"
cluster_name = "eshop-eks-cluster"
environment  = "prod"
cluster_version = "1.32"
vpc_cidr = "10.0.0.0/16"

kms_alias_name = "alias/eks/eshop-eks-cluster"

subnets_config = {
  # --- PUBLIC SUBNETS ---
  "public-2a" = {
    type = "public"
    az   = "ap-south-2a"
    cidr = "10.0.1.0/24"
    name = "eshop-public-ap-south-2a"
  },
  "public-2b" = {
    type = "public"
    az   = "ap-south-2b"
    cidr = "10.0.2.0/24"
    name = "eshop-public-ap-south-2b"
  }, 

  # --- PRIVATE SUBNETS ---
  "private-2a" = {
    type = "private"
    az   = "ap-south-2a"
    cidr = "10.0.11.0/24"
    name = "eshop-private-ap-south-2a"
  },
  "private-2b" = {
    type = "private"
    az   = "ap-south-2b"
    cidr = "10.0.12.0/24"
    name = "eshop-private-ap-south-2b"
  }
}