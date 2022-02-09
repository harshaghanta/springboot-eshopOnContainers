package com.eshoponcontainers.catalogapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CatalogType")
@Data
public class CatalogType {
    
    @Id
    @Column(name = "Id")
    private int id;

    @Column(name = "Type", nullable = false, columnDefinition = "nvarchar(100)")
    private String type;
}
