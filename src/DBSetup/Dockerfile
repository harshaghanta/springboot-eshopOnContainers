FROM mcr.microsoft.com/mssql/server:2022-CU13-ubuntu-22.04
#Added to bypass the file permission issue
USER root
# 
# RUN chmod 755 /usr
#RUN mkdir -p /usr/dbscripts
# WORKDIR /usr/dbscripts

# COPY --chown=root DBSetup/scripts .


RUN mkdir -p /var/opt/mssql/data /usr/src/app

# Copy the .mdf and .ldf files to the container 
COPY DBSetup/data/*.mdf /var/opt/mssql/data/
COPY DBSetup/data/*.ldf /var/opt/mssql/data/

# Copy the attach-db script COPY
COPY DBSetup/scripts/ /usr/src/app/

RUN chmod +x /usr/src/app/db-init.sh
RUN chmod +x /usr/src/app/entrypoint.sh
RUN chmod +x /usr/src/app/healthcheck.sh

CMD /bin/bash /usr/src/app/entrypoint.sh

# Run SQL Server and the attach script 
# CMD /bin/bash -c "sleep 90 && /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P \"$SA_PASSWORD\" -i /usr/src/app/attach-db.sql && /opt/mssql/bin/sqlservr"

# Run Microsoft SQl Server and initialization script (at the same time)
# Note: If you want to start MsSQL only (without initialization script) you can comment bellow line out, CMD entry from base image will be taken

HEALTHCHECK --interval=30s --timeout=30s --start-period=120s --retries=3 CMD /bin/bash -c "/usr/src/app/healthcheck.sh" 

# CMD /bin/bash ./entrypoint.sh

