package com.eshoponcontainers.catalogapi.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
@Table(name = "Catalog")
public class CatalogItem {
    @Id
    @Column(name = "Id")
    private int id;

    @Column(name = "Name", nullable = false, columnDefinition = "nvarchar(50)")
    private String name;

    @Column(name = "Description", nullable = true, columnDefinition = "nvarchar(max)")
    private String description;

    @Column(name = "Price", nullable = false, columnDefinition = "decimal(18,2)")
    private BigDecimal price;

    @Column(name = "PictureFileName", nullable = true, columnDefinition = "nvarchar(max)")
    private String pictureFileName;

    @Transient
    private String pictureUri;

    @ManyToOne
    @JoinColumn(name = "CatalogTypeId", referencedColumnName = "Id")
    private CatalogType catalogType;

    @ManyToOne
    @JoinColumn(name="CatalogBrandId", referencedColumnName="Id")
    private CatalogBrand catalogBrand;

    @Column(name = "AvailableStock", nullable = false)
    private int availableStock;

    @Column(name = "RestockThreshold", nullable = false)
    private int restockThreshold;

    @Column(name = "MaxStockThreshold", nullable = false)
    private int maxStockThreshold;

    @Column(name = "OnReorder", nullable = false)
    private boolean onReorder;
}
