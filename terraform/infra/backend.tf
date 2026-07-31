terraform {
  required_version = ">= 1.10.0"

  backend "s3" {
    bucket       = "my-eshop-tfstate-bucket" # Shared S3 bucket
    key          = "infra/terraform.tfstate" # Unique path for infra
    region       = "ap-south-2"
    encrypt      = true
    use_lockfile = true                      # S3 native locking
  }
}