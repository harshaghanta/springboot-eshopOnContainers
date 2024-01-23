package com.eshoponcontainers.catalogapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "CatalogBrand")
public class CatalogBrand {

    @Id
    @Column(name = "Id")
    private int id;

    @Column(name = "Brand", nullable = false, columnDefinition = "nvarchar(100)")
    private String brand;
}
