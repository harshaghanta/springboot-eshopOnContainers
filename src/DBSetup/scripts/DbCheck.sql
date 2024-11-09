SET nocount on;
DECLARE @myVariable BIT;
IF ((SELECT COUNT(1) FROM sys.databases WHERE name IN ('CatalogDB', 'OrderDB')) = 2)
BEGIN
    SET @myVariable = 1    
END

-- INSERT INTO [ordering].[cardtypes]([Name]) VALUES('HealthCheckDummy');

IF @myVariable = 1
BEGIN
    IF NOT EXISTS (SELECT * FROM CatalogDB.INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'CatalogType')
    BEGIN
        SET @myVariable = 0
    END
END

IF @myVariable = 1
BEGIN    
    IF NOT EXISTS (SELECT * FROM OrderDB.INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'cardtypes')
    BEGIN
        SET @myVariable = 0
    END
END

SELECT  @myVariable