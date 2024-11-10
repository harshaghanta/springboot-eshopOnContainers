#!/bin/bash

echo 'entered healthcheck.sh script'

set -eo pipefail

host="$(hostname --ip-address || echo '127.0.0.1')"
user=SA
password=$SA_PASSWORD


# Construct the sqlcmd command
sqlcmd_command=(
    /opt/mssql-tools/bin/sqlcmd
    -S "$host"
    -U "$user"
    -P "$password"
    -d master
    -i /usr/src/app/DbCheck.sql
    -h -1
)


echo 'executing the healthcheck.sh script'

# Assign the output of the sqlcmd_command to a variable
select="$("${sqlcmd_command[@]}")"

# Print the variable
echo "sqlcmd output: $select"

if [ "$select" = 1 ]; then
    echo 'Healthcheck passed. returning exit code 0'
    exit 0
fi
echo 'Healthcheck failed.returning exit code 1'
exit 1