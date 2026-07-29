variable "aws_region" {
  description = "AWS region for resources"
  type        = string
  default     = "ap-south-2"
}

variable "eks_cluster_name" {
  description = "Name of the existing EKS cluster"
  type        = string
}

variable "db_identifier" {
  description = "Identifier for the RDS SQL Server instance"
  type        = string
  default     = "eshop-sqlserver-db"
}

variable "db_allocated_storage" {
  description = "Allocated storage in GB for the RDS instance"
  type        = number
  default     = 20
}

variable "db_engine_version" {
  description = "SQL Server Engine Version"
  type        = string
  default     = "2022.00"
}

variable "db_instance_class" {
  description = "Instance class for RDS SQL Server"
  type        = string
  default     = "db.t3.micro"
}

variable "db_username" {
  description = "Master username for the RDS database"
  type        = string
  default     = "adminuser"
}

variable "db_password" {
  description = "Master password for the RDS database"
  type        = string
  sensitive   = true
}

variable "sql_script_path" {
  description = "Relative path to the database initialization SQL script"
  type        = string
  default     = "EshopDB.sql"
}