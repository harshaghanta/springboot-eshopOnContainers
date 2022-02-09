package com.eshoponcontainers.catalogapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



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
