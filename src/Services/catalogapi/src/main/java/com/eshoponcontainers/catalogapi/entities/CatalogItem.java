package com.eshoponcontainers.catalogapi.entities;

import java.math.BigDecimal;

import com.eshoponcontainers.catalogapi.excep.CatalogDomainException;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "Catalog")
public class CatalogItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

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

    // @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "CatalogTypeId", referencedColumnName = "Id")
    private CatalogType catalogType;

    // @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "CatalogBrandId", referencedColumnName = "Id")
    private CatalogBrand catalogBrand;

    @Column(name = "AvailableStock", nullable = false)
    private int availableStock;

    @Column(name = "RestockThreshold", nullable = false)
    private int restockThreshold;

    @Column(name = "MaxStockThreshold", nullable = false)
    private int maxStockThreshold;

    @Column(name = "OnReorder", nullable = false)
    private boolean onReorder;

    @Transient
    private Integer catalogBrandId;

    @Transient
    private Integer catalogTypeId;

    // @JsonProperty    
    // @JsonIgnore
    public Integer getCatalogBrandId() {
        return this.catalogBrand == null ? null : this.catalogBrand.getId();
    }

    // @JsonProperty
    public Integer getCatalogTypeId() {
        return this.catalogType == null ? null : this.catalogType.getId();
    }

    // public void setCatalogTypeId(int value) {
    //     catalogTypeId = value;
    // }

    // public void setCatalogBrandId(int value) {
    //     catalogBrandId = value;
    // }

    // public void setCatalogType(CatalogType value) {
    // catalogType = value;
    // }

    // public void setCatalogBrand(CatalogBrand value) {
    // catalogBrand = value;
    // }

    public int removeStock(int quantityDesired) {

        if (this.availableStock == 0) {
            throw new CatalogDomainException(String.format("Empty stock, product item %s is sold out", this.name));
        }

        if (quantityDesired <= 0) {
            throw new CatalogDomainException("Item units desired should be greater than zero");
        }

        int removed = Math.min(quantityDesired, this.availableStock);
        this.setAvailableStock(this.availableStock - removed);
        return removed;
    }

    // public int addStock(int quantity) {
    //     int original = this.availableStock;

    //     if (this.availableStock + quantity > this.maxStockThreshold) {
    //         this.availableStock += (this.maxStockThreshold - this.availableStock);
    //     } else {
    //         this.availableStock += quantity;
    //     }

    //     this.onReorder = false;
    //     return this.availableStock - quantity;
    // }

}
