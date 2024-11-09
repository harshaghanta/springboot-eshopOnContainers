#!/bin/bash

# Start SQL Server in the background
/opt/mssql/bin/sqlservr &

# Capture the PID of the SQL Server process
SQLSERVR_PID=$!

# Wait for 30 seconds to ensure SQL Server is up and running
sleep 30s

# Run the initialization script
/usr/src/app/db-init.sh

# Wait for the SQL Server process to finish
wait $SQLSERVR_PID