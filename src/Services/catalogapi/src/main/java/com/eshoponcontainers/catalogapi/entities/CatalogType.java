package com.eshoponcontainers.catalogapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
