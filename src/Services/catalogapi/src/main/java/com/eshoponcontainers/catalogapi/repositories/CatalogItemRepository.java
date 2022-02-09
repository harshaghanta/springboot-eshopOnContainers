package com.eshoponcontainers.catalogapi.repositories;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogItemRepository extends JpaRepository<CatalogItem, Integer> {

}
