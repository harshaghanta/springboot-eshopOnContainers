terraform {
  required_version = ">= 1.3.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.20"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# 1. Fetch Existing EKS Cluster Info and Network Context
data "aws_eks_cluster" "eks" {
  name = var.eks_cluster_name
}

data "aws_vpc" "selected" {
  id = data.aws_eks_cluster.eks.vpc_config[0].vpc_id
}

# Fetch private subnets associated with the EKS VPC
data "aws_subnets" "private" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.selected.id]
  }

  filter {
    name   = "tag:kubernetes.io/role/internal-elb"
    values = ["1"]
  }
}

# Provider setup to execute DB Bootstrap Job directly inside the EKS cluster
provider "kubernetes" {
  host                   = data.aws_eks_cluster.eks.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.eks.certificate_authority[0].data)
  exec {
    api_version = "client.authentication.k8s.io/v1beta1"
    args        = ["eks", "get-token", "--cluster-name", data.aws_eks_cluster.eks.name]
    command     = "aws"
  }
}

# 2. Database Subnet Group in EKS VPC Private Subnets
resource "aws_db_subnet_group" "rds_subnet_group" {
  name        = "${var.db_identifier}-subnet-group"
  subnet_ids  = data.aws_subnets.private.ids
  description = "Subnet group for ${var.db_identifier} SQL Server instance"
}

# 3. Security Group Restricting Access ONLY to the EKS Cluster Security Group
resource "aws_security_group" "rds_sg" {
  name        = "${var.db_identifier}-sg"
  description = "Allow inbound MSSQL traffic from EKS cluster only"
  vpc_id      = data.aws_vpc.selected.id

  ingress {
    description     = "MSSQL port access from EKS Cluster SG"
    from_port       = 1433
    to_port         = 1433
    protocol        = "tcp"
    cidr_blocks = [data.aws_vpc.selected.cidr_block]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# 4. Provision RDS SQL Server Instance
resource "aws_db_instance" "sqlserver" {
  identifier             = var.db_identifier
  allocated_storage      = var.db_allocated_storage
  engine                 = "sqlserver-ex"
  engine_version         = var.db_engine_version
  instance_class         = var.db_instance_class
  username               = var.db_username
  password               = var.db_password
  db_subnet_group_name   = aws_db_subnet_group.rds_subnet_group.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  publicly_accessible    = false
  skip_final_snapshot    = true
}

locals {
  # Determine the valid file path safely
  sql_file_path = fileexists(var.sql_script_path) ? var.sql_script_path : "${path.module}/${var.sql_script_path}"
}

# 5. Execute DB Bootstrap Script via a Kubernetes Job inside EKS
# (Guarantees script runs from within the private network context)
resource "kubernetes_config_map" "db_script" {
  metadata {
    name      = "db-bootstrap-script"
    namespace = "default"
  }

  data = {
    "EshopDB.sql" = file("${path.module}/${var.sql_script_path}")
  }
}

resource "kubernetes_job" "db_bootstrap_job" {
  metadata {
    name      = "db-bootstrap-job"
    namespace = "default"
  }

  spec {
    template {
      metadata {
        name = "db-bootstrap-runner"
      }
      spec {
        restart_policy = "Never"

        container {
          name  = "sqlcmd"
          image = "alpine:latest"
          command = [
            "/bin/sh",
            "-c",
            <<-EOT
              apk add --no-cache curl tar bzip2 && \
              curl -L https://github.com/microsoft/go-sqlcmd/releases/download/v1.8.2/sqlcmd-linux-arm64.tar.bz2 | tar -xj -C /tmp && \
              /tmp/sqlcmd -S ${aws_db_instance.sqlserver.endpoint} -U ${var.db_username} -P '${var.db_password}' -i /scripts/EshopDB.sql
            EOT
          ]

          volume_mount {
            name       = "script-vol"
            mount_path = "/scripts"
          }
        }

        volume {
          name = "script-vol"
          config_map {
            name = kubernetes_config_map.db_script.metadata[0].name
          }
        }
      }
    }
  }

  wait_for_completion = true

  timeouts {
    create = "5m"
  }

  depends_on = [aws_db_instance.sqlserver, kubernetes_config_map.db_script]
}