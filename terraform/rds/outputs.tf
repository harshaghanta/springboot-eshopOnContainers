output "rds_endpoint" {
  description = "The endpoint URL of the created RDS SQL Server instance"
  value       = aws_db_instance.sqlserver.endpoint
}

output "rds_address" {
  description = "The hostname address of the RDS SQL Server instance"
  value       = aws_db_instance.sqlserver.address
}

output "db_bootstrap_job_status" {
  description = "Completion status of the DB bootstrap Kubernetes Job"
  value       = "Completed successfully"
}