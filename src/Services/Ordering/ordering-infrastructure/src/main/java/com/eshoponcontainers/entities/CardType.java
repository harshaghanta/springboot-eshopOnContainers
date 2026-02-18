package com.eshoponcontainers.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "[ordering].[cardtypes]")
public class CardType extends BaseEntity {
    private String name;

    // Getters and setters...
}