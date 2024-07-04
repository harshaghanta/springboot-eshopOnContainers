FROM mcr.microsoft.com/mssql/server:2019-latest
#Added to bypass the file permission issue
USER root
# 
# RUN chmod 755 /usr
#RUN mkdir -p /usr/dbscripts
WORKDIR /usr/dbscripts

COPY --chown=root DBSetup/scripts .
RUN chmod +x ./db-init.sh
RUN chmod +x ./entrypoint.sh

# Run Microsoft SQl Server and initialization script (at the same time)
# Note: If you want to start MsSQL only (without initialization script) you can comment bellow line out, CMD entry from base image will be taken

CMD /bin/bash ./entrypoint.sh

HEALTHCHECK --interval=30s --timeout=30s --start-period=30s --retries=3 CMD [ "/opt/mssql-tools/bin/sqlcmd", "-S", "localhost", "-U", "sa", "-P", "Pass@word", "-d", "master", "-i", "DbCheck.sql" ]
