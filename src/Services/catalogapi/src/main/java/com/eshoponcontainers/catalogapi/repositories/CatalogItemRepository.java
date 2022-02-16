package com.eshoponcontainers.catalogapi.repositories;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogItemRepository extends JpaRepository<CatalogItem, Integer> {

    Page<CatalogItem> findByNameStartsWith(String name, Pageable pageable);

    Page<CatalogItem> findAllByCatalogType_Id(Integer catalogTypeId, Pageable pageable);

    Page<CatalogItem> findByCatalogType_IdAndCatalogBrand_Id(Integer catalogTypeId, Integer catalogBrandId,
    Pageable pageable);

}
