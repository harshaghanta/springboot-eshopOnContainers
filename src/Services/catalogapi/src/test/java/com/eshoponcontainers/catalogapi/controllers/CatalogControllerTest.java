package com.eshoponcontainers.catalogapi.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.services.CatalogService;

@ExtendWith(MockitoExtension.class)
class CatalogControllerTest {

    @Mock
    private CatalogService catalogService;

    @InjectMocks
    private CatalogController catalogController;

    @Test
    void getItemsById_whenIdIsInvalid_returnsBadRequest() {
        ResponseEntity<CatalogItem> response = catalogController.getItemsById(-1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getItemsById_whenItemExists_returnsOk() {
        CatalogItem item = new CatalogItem();
        item.setId(5);
        item.setName("Laptop");
        item.setPrice(new BigDecimal("899.99"));

        when(catalogService.getItemById(5)).thenReturn(Optional.of(item));

        ResponseEntity<CatalogItem> response = catalogController.getItemsById(5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Laptop", response.getBody().getName());
    }

    @Test
    void updateProduct_whenServiceReturnsFalse_returnsNotFound() {
        CatalogItem item = new CatalogItem();
        item.setId(99);
        item.setPrice(new BigDecimal("50.00"));

        when(catalogService.updateProduct(item)).thenReturn(false);

        ResponseEntity<Object> response = catalogController.updateProduct(item);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
