USE [master];
GO 

CREATE DATABASE [CatalogDB] ON (
        FILENAME = '/var/opt/mssql/data/CatalogDB.mdf'
    ),
    (
        FILENAME = '/var/opt/mssql/data/CatalogDB_log.ldf'
    ) FOR ATTACH;
GO

CREATE DATABASE [OrderDB] ON (
        FILENAME = '/var/opt/mssql/data/OrderDB.mdf'
    ),
    (
        FILENAME = '/var/opt/mssql/data/OrderDB_log.ldf'
    ) FOR ATTACH;
GO