package com.eshoponcontainers.catalogapi.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.eshoponcontainers.catalogapi.excep.CatalogDomainException;

class CatalogItemTest {

    @Test
    void removeStock_whenStockIsEmpty_throwsException() {
        CatalogItem item = new CatalogItem();
        item.setName("Keyboard");
        item.setAvailableStock(0);

        CatalogDomainException exception = assertThrows(CatalogDomainException.class,
                () -> item.removeStock(1));

        assertEquals("Empty stock, product item Keyboard is sold out", exception.getMessage());
    }

    @Test
    void removeStock_whenQuantityIsInvalid_throwsException() {
        CatalogItem item = new CatalogItem();
        item.setName("Mouse");
        item.setAvailableStock(2);

        CatalogDomainException exception = assertThrows(CatalogDomainException.class,
                () -> item.removeStock(0));

        assertEquals("Item units desired should be greater than zero", exception.getMessage());
    }

    @Test
    void removeStock_whenQuantityIsAvailable_reducesStock() {
        CatalogItem item = new CatalogItem();
        item.setName("Monitor");
        item.setAvailableStock(5);

        int removed = item.removeStock(3);

        assertEquals(3, removed);
        assertEquals(2, item.getAvailableStock());
    }
}
