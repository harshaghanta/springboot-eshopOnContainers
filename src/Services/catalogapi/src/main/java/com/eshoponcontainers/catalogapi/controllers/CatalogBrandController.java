package com.eshoponcontainers.catalogapi.controllers;

import java.util.List;

import com.eshoponcontainers.catalogapi.entities.CatalogBrand;
import com.eshoponcontainers.catalogapi.repositories.CatalogBrandRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogBrandController {

    @Autowired
    private CatalogBrandRepository catalogBrandRepository;
    
    @GetMapping("/brands")
    public ResponseEntity<List<CatalogBrand>> getBrands() {
        return ResponseEntity.ok(catalogBrandRepository.findAll());
    }

}
