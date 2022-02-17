package com.eshoponcontainers.catalogapi.repositories;

import com.eshoponcontainers.catalogapi.entities.CatalogType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogTypeRepository extends JpaRepository<CatalogType, Integer>  {
    
}
