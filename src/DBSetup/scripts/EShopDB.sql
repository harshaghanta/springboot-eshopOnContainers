IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'CatalogDB')
BEGIN
	CREATE DATABASE CatalogDB;
END
GO

IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'OrderDB')
BEGIN
	CREATE DATABASE OrderDB;
END
GO

USE [CatalogDB]
GO
/****** Object:  Table [dbo].[Catalog]    Script Date: 3/20/2022 7:28:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Catalog]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Catalog](
	[Id] [int] Identity(1,1) NOT NULL,
	[AvailableStock] [int] NOT NULL,
	[Description] [nvarchar](max) NULL,
	[MaxStockThreshold] [int] NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[OnReorder] [bit] NOT NULL,
	[PictureFileName] [nvarchar](max) NULL,
	[Price] [decimal](18, 2) NOT NULL,
	[RestockThreshold] [int] NOT NULL,
	[CatalogBrandId] [int] NULL,
	[CatalogTypeId] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[CatalogBrand]    Script Date: 3/20/2022 7:28:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[CatalogBrand]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[CatalogBrand](
	[Id] [int] NOT NULL,
	[Brand] [nvarchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[CatalogType]    Script Date: 3/20/2022 7:28:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[CatalogType]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[CatalogType](
	[Id] [int] NOT NULL,
	[Type] [nvarchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[IntegrationEventLog]    Script Date: 3/20/2022 7:28:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[IntegrationEventLog]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[IntegrationEventLog](
	[EventId] [uniqueidentifier] NOT NULL,
	[Content] [nvarchar](max) NOT NULL,
	[CreationTime] [datetime2](7) NOT NULL,
	[EventTypeName] [nvarchar](max) NOT NULL,
	[State] [int] NOT NULL,
	[TimesSent] [int] NOT NULL,
	[TransactionId] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[EventId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK20xg7a5wq0gllbnknf5rg31oo]') AND parent_object_id = OBJECT_ID(N'[dbo].[Catalog]'))
	ALTER TABLE [dbo].[Catalog]  WITH CHECK ADD  CONSTRAINT [FK20xg7a5wq0gllbnknf5rg31oo] FOREIGN KEY([CatalogBrandId])
	REFERENCES [dbo].[CatalogBrand] ([Id])
GO

IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK20xg7a5wq0gllbnknf5rg31oo]') AND parent_object_id = OBJECT_ID(N'[dbo].[Catalog]'))
	ALTER TABLE [dbo].[Catalog] CHECK CONSTRAINT [FK20xg7a5wq0gllbnknf5rg31oo]
GO

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK8d255nh6w9gcit9xpm4kblao]') AND parent_object_id = OBJECT_ID(N'[dbo].[Catalog]'))
	ALTER TABLE [dbo].[Catalog]  WITH CHECK ADD  CONSTRAINT [FK8d255nh6w9gcit9xpm4kblao] FOREIGN KEY([CatalogTypeId])
	REFERENCES [dbo].[CatalogType] ([Id])
GO

IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK8d255nh6w9gcit9xpm4kblao]') AND parent_object_id = OBJECT_ID(N'[dbo].[Catalog]'))
	ALTER TABLE [dbo].[Catalog] CHECK CONSTRAINT [FK8d255nh6w9gcit9xpm4kblao]
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM [dbo].[CatalogBrand])
BEGIN
	INSERT [dbo].[CatalogBrand] ([Id], [Brand]) VALUES (1, N'.NET')
	INSERT [dbo].[CatalogBrand] ([Id], [Brand]) VALUES (2, N'Other')
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM [dbo].[CatalogType])
BEGIN
	INSERT [dbo].[CatalogType] ([Id], [Type]) VALUES (1, N'Mug')
	INSERT [dbo].[CatalogType] ([Id], [Type]) VALUES (2, N'T-Shirt')
	INSERT [dbo].[CatalogType] ([Id], [Type]) VALUES (3, N'Pin')
END
GO


SET IDENTITY_INSERT dbo.Catalog ON;  
GO  

IF NOT EXISTS(SELECT TOP 1 1 FROM [dbo].[Catalog])
BEGIN
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (1, 100, N'.NET Bot Black Hoodie, and more', 0, N'.NET Bot Black Hoodie', 0, N'1.png', CAST(199.50 AS Decimal(18, 2)), 0, 1, 2)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (2, 89, N'.NET Black & White Mug', 0, N'.NET Black & White Mug', 1, N'2.png', CAST(8.50 AS Decimal(18, 2)), 0, 1, 1)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (3, 56, N'Prism White T-Shirt', 0, N'Prism White T-Shirt', 0, N'3.png', CAST(12.00 AS Decimal(18, 2)), 0, 2, 2)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (4, 120, N'.NET Foundation T-shirt', 0, N'.NET Foundation T-shirt', 0, N'4.png', CAST(12.00 AS Decimal(18, 2)), 0, 1, 2)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (5, 55, N'Roslyn Red Pin', 0, N'Roslyn Red Pin', 0, N'5.png', CAST(8.50 AS Decimal(18, 2)), 0, 2, 3)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (6, 17, N'.NET Blue Hoodie', 0, N'.NET Blue Hoodie', 0, N'6.png', CAST(12.00 AS Decimal(18, 2)), 0, 1, 2)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (7, 8, N'Roslyn Red T-Shirt', 0, N'Roslyn Red T-Shirt', 0, N'7.png', CAST(12.00 AS Decimal(18, 2)), 0, 2, 2)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (8, 34, N'Kudu Purple Hoodie', 0, N'Kudu Purple Hoodie', 0, N'8.png', CAST(8.50 AS Decimal(18, 2)), 0, 2, 2)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (9, 76, N'Cup<T> White Mug', 0, N'Cup<T> White Mug', 0, N'9.png', CAST(12.00 AS Decimal(18, 2)), 0, 2, 1)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (10, 11, N'.NET Foundation Pin', 0, N'.NET Foundation Pin', 0, N'10.png', CAST(12.00 AS Decimal(18, 2)), 0, 1, 3)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (11, 3, N'Cup<T> Pin', 0, N'Cup<T> Pin', 0, N'11.png', CAST(8.50 AS Decimal(18, 2)), 0, 1, 3)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (12, 0, N'Prism White TShirt', 0, N'Prism White TShirt', 0, N'12.png', CAST(12.00 AS Decimal(18, 2)), 0, 2, 2)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (13, 89, N'Modern .NET Black & White Mug', 0, N'Modern .NET Black & White Mug', 1, N'13.png', CAST(8.50 AS Decimal(18, 2)), 0, 1, 1)
	INSERT [dbo].[Catalog] ([Id], [AvailableStock], [Description], [MaxStockThreshold], [Name], [OnReorder], [PictureFileName], [Price], [RestockThreshold], [CatalogBrandId], [CatalogTypeId]) VALUES (14, 76, N'Modern Cup<T> White Mug', 0, N'Modern Cup<T> White Mug', 0, N'14.png', CAST(12.00 AS Decimal(18, 2)), 0, 2, 1)
END
GO

SET IDENTITY_INSERT dbo.Catalog OFF;  
GO  



IF NOT EXISTS(SELECT TOP 1 1 FROM [dbo].[IntegrationEventLog])
BEGIN
	INSERT [dbo].[IntegrationEventLog] ([EventId], [Content], [CreationTime], [EventTypeName], [State], [TimesSent], [TransactionId]) VALUES (N'a4fe50e7-1bc9-8147-afe4-34f111d48d16', N'{"id":"e750fea4-c91b-4781-afe4-34f111d48d16","creationDate":1645883052988,"productId":1,"newPrice":19.50,"oldPrice":39.50}', CAST(N'2022-02-26T19:14:12.9880000' AS DateTime2), N'com.eshoponcontainers.catalogapi.integrationevents.ProductPriceChangedIntegrationEvent', 0, 0, N'437a7bc7-f06b-424b-9326-bbafa9c81a51')
	INSERT [dbo].[IntegrationEventLog] ([EventId], [Content], [CreationTime], [EventTypeName], [State], [TimesSent], [TransactionId]) VALUES (N'23f02909-70a4-ec4a-a6b4-364007072ac3', N'{"id":"0929f023-a470-4aec-a6b4-364007072ac3","creationDate":1647677606472,"productId":1,"newPrice":137.50,"oldPrice":129.50}', CAST(N'2022-03-19T13:43:26.4720000' AS DateTime2), N'com.eshoponcontainers.catalogapi.integrationevents.ProductPriceChangedIntegrationEvent', 0, 0, N'1cc9ed88-5d2e-4ef9-9f18-1db5ed69cbaa')
	INSERT [dbo].[IntegrationEventLog] ([EventId], [Content], [CreationTime], [EventTypeName], [State], [TimesSent], [TransactionId]) VALUES (N'03495298-7966-4a4c-acb6-5d5b8f3d38b2', N'{"id":"98524903-6679-4c4a-acb6-5d5b8f3d38b2","creationDate":1645883080075,"productId":1,"newPrice":23.50,"oldPrice":19.50}', CAST(N'2022-02-26T19:14:40.0750000' AS DateTime2), N'com.eshoponcontainers.catalogapi.integrationevents.ProductPriceChangedIntegrationEvent', 0, 0, N'56e13c77-4114-4674-bebe-16b79f97e184')
	INSERT [dbo].[IntegrationEventLog] ([EventId], [Content], [CreationTime], [EventTypeName], [State], [TimesSent], [TransactionId]) VALUES (N'fef883cf-cc4a-c344-a0bd-6f40461d4e9f', N'{"id":"cf83f8fe-4acc-44c3-a0bd-6f40461d4e9f","creationDate":1647696073101,"productId":1,"newPrice":199.50,"oldPrice":99.50}', CAST(N'2022-03-19T18:51:13.1010000' AS DateTime2), N'com.eshoponcontainers.catalogapi.integrationevents.events.ProductPriceChangedIntegrationEvent', 2, 1, N'e524a6d6-1632-4d79-844c-d6786938c267')
	INSERT [dbo].[IntegrationEventLog] ([EventId], [Content], [CreationTime], [EventTypeName], [State], [TimesSent], [TransactionId]) VALUES (N'614050c7-6ca6-5247-bb1d-a9c8fbc0a15f', N'{"id":"c7504061-a66c-4752-bb1d-a9c8fbc0a15f","creationDate":1647691351251,"productId":1,"newPrice":124.50,"oldPrice":137.50}', CAST(N'2022-03-19T17:32:31.2510000' AS DateTime2), N'com.eshoponcontainers.catalogapi.integrationevents.ProductPriceChangedIntegrationEvent', 2, 1, N'88aab933-2767-4ab8-8281-46aa4cc7628d')
	INSERT [dbo].[IntegrationEventLog] ([EventId], [Content], [CreationTime], [EventTypeName], [State], [TimesSent], [TransactionId]) VALUES (N'cfec0a20-f7ef-d041-af46-acdb4e43b571', N'{"id":"200aeccf-eff7-41d0-af46-acdb4e43b571","creationDate":1647695924807,"productId":1,"newPrice":99.50,"oldPrice":124.50}', CAST(N'2022-03-19T18:48:44.8070000' AS DateTime2), N'com.eshoponcontainers.catalogapi.integrationevents.events.ProductPriceChangedIntegrationEvent', 2, 1, N'12910cb0-20e3-47d5-b9c7-bc2576e9f0c1')
END
GO

USE OrderDB;
GO
IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'ordering')
BEGIN
	EXEC('CREATE SCHEMA ordering')
END
GO

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[ordering].[buyers]') AND type in (N'U'))
BEGIN
	CREATE TABLE [ordering].[buyers](
		[Id] [int] IDENTITY(1,1) PRIMARY KEY,
		[IdentityGuid] [nvarchar](200) NOT NULL,
		[Name] [nvarchar](max) NULL
	); 
END
GO

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[ordering].[cardtypes]') AND type in (N'U'))
BEGIN
	CREATE TABLE [ordering].[cardtypes](
		[Id] [int] IDENTITY(1,1) PRIMARY KEY,
		[Name] [nvarchar](200) NOT NULL
	);
END
GO

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[ordering].[orderstatus]') AND type in (N'U'))
BEGIN
	CREATE TABLE [ordering].[orderstatus](
		[Id] [int] IDENTITY(1,1) PRIMARY KEY,
		[Name] [nvarchar](200) NOT NULL
	);
END
GO

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[ordering].[paymentmethods]') AND type in (N'U'))
BEGIN
	CREATE TABLE [ordering].[paymentmethods](
		[Id] [int] IDENTITY(1,1) PRIMARY KEY,
		[Alias] [nvarchar](200) NOT NULL,
		[BuyerId] [int] FOREIGN KEY REFERENCES [ordering].[buyers]([Id]) NOT NULL,
		[CardHolderName] [nvarchar](200) NOT NULL,
		[CardNumber] [nvarchar](25) NOT NULL,
		[CardTypeId] [int] FOREIGN KEY REFERENCES [ordering].[cardtypes]([Id]) NOT NULL,
		[Expiration] [datetime2](7) NOT NULL
	);
END
GO

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[ordering].[requests]') AND type in (N'U'))
BEGIN
	CREATE TABLE [ordering].[requests](
		[Id] [uniqueidentifier] PRIMARY KEY,
		[Name] [nvarchar](max) NOT NULL,
		[Time] [datetime2](7) NOT NULL
	);
END
GO

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[ordering].[orders]') AND type in (N'U'))
BEGIN
	CREATE TABLE [ordering].[orders](
		[Id] [int] IDENTITY(1,1) PRIMARY KEY,
		[BuyerId] [int] FOREIGN KEY REFERENCES [ordering].[buyers]([Id]) NULL,
		[OrderDate] [datetime2](7) NOT NULL,
		[OrderStatusId] [int] FOREIGN KEY REFERENCES [ordering].[orderstatus]([Id]) NOT NULL,
		[PaymentMethodId] [int] NULL,
		[Description] [nvarchar](max) NULL,
		[Address_City] [nvarchar](max) NULL,
		[Address_Country] [nvarchar](max) NULL,
		[Address_State] [nvarchar](max) NULL,
		[Address_Street] [nvarchar](max) NULL,
		[Address_ZipCode] [nvarchar](max) NULL
	);
END
GO

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[ordering].[orderItems]') AND type in (N'U'))
BEGIN
	CREATE TABLE [ordering].[orderItems](
		[Id] [int] IDENTITY(1,1) PRIMARY KEY,
		[Discount] [decimal](18, 2) NOT NULL,
		[OrderId] [int] REFERENCES [ordering].[orders]([Id]) NULL,
		[PictureUrl] [nvarchar](max) NULL,
		[ProductId] [int] NOT NULL,
		[ProductName] [nvarchar](max) NOT NULL,
		[UnitPrice] [decimal](18, 2) NOT NULL,
		[Units] [int] NOT NULL
	);
END
GO

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[IntegrationEventLog]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[IntegrationEventLog](
	[EventId] [uniqueidentifier] NOT NULL,
	[Content] [nvarchar](max) NOT NULL,
	[CreationTime] [datetime2](7) NOT NULL,
	[EventTypeName] [nvarchar](max) NOT NULL,
	[State] [int] NOT NULL,
	[TimesSent] [int] NOT NULL,
	[TransactionId] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[EventId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM [ordering].[orderstatus])
BEGIN
	INSERT [ordering].[orderstatus] ([Name]) VALUES('submitted');
	INSERT [ordering].[orderstatus] ([Name]) VALUES('awaitingvalidation');
	INSERT [ordering].[orderstatus] ([Name]) VALUES('stockconfirmed');
	INSERT [ordering].[orderstatus] ([Name]) VALUES('paid');
	INSERT [ordering].[orderstatus] ([Name]) VALUES('shipped');
	INSERT [ordering].[orderstatus] ([Name]) VALUES('cancelled');
END
GO



IF NOT EXISTS(SELECT * FROM sys.indexes WHERE name = 'IX_buyers_IdentityGuid' AND object_id = OBJECT_ID(N'[ordering].[buyers]'))
BEGIN
	CREATE UNIQUE NONCLUSTERED INDEX [IX_buyers_IdentityGuid] ON [ordering].[buyers]
	(
		[IdentityGuid] ASC
	)
END
GO

IF NOT EXISTS(SELECT * FROM sys.indexes WHERE name = 'IX_orderItems_OrderId' AND object_id = OBJECT_ID(N'[ordering].[orderItems]'))
BEGIN
	CREATE NONCLUSTERED INDEX [IX_orderItems_OrderId] ON [ordering].[orderItems]
	(
		[OrderId] ASC
	)
END
GO

IF NOT EXISTS(SELECT * FROM sys.indexes WHERE name = 'IX_orders_BuyerId' AND object_id = OBJECT_ID(N'[ordering].[orders]'))
BEGIN
	CREATE NONCLUSTERED INDEX [IX_orders_BuyerId] ON [ordering].[orders]
	(
		[BuyerId] ASC
	)
END
GO


IF NOT EXISTS(SELECT * FROM sys.indexes WHERE name = 'IX_orders_OrderStatusId' AND object_id = OBJECT_ID(N'[ordering].[orders]'))
BEGIN
	CREATE NONCLUSTERED INDEX [IX_orders_OrderStatusId] ON [ordering].[orders]
	(
		[OrderStatusId] ASC
	)
END
GO

IF NOT EXISTS(SELECT * FROM sys.indexes WHERE name = 'IX_orders_PaymentMethodId' AND object_id = OBJECT_ID(N'[ordering].[orders]'))
BEGIN
	CREATE NONCLUSTERED INDEX [IX_orders_PaymentMethodId] ON [ordering].[orders]
	(
		[PaymentMethodId] ASC
	)
END
GO


IF NOT EXISTS(SELECT * FROM sys.indexes WHERE name = 'IX_paymentmethods_BuyerId' AND object_id = OBJECT_ID(N'[ordering].[paymentmethods]'))
BEGIN
	CREATE NONCLUSTERED INDEX [IX_paymentmethods_BuyerId] ON [ordering].[paymentmethods]
	(
		[BuyerId] ASC
	)
END
GO

IF NOT EXISTS(SELECT * FROM sys.indexes WHERE name = 'IX_paymentmethods_CardTypeId' AND object_id = OBJECT_ID(N'[ordering].[paymentmethods]'))
BEGIN
	CREATE NONCLUSTERED INDEX [IX_paymentmethods_CardTypeId] ON [ordering].[paymentmethods]
	(
		[CardTypeId] ASC
	)
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM [ordering].cardtypes)
BEGIN
	INSERT INTO [ordering].[cardtypes]([Name]) VALUES('Amex');
	INSERT INTO [ordering].[cardtypes]([Name]) VALUES('Visa');
	INSERT INTO [ordering].[cardtypes]([Name]) VALUES('MasterCard');
END