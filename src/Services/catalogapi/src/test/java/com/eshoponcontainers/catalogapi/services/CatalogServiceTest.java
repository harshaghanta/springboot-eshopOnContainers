package com.eshoponcontainers.catalogapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.repositories.CatalogBrandRepository;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.catalogapi.repositories.CatalogTypeRepository;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private CatalogItemRepository catalogItemRepository;

    @Mock
    private CatalogTypeRepository catalogTypeRepository;

    @Mock
    private CatalogBrandRepository catalogBrandRepository;

    @Mock
    private CatalogIntegrationService catalogIntegrationService;

    private CatalogService catalogService;

    @BeforeEach
    void setUp() throws Exception {
        catalogService = new CatalogService(
                catalogItemRepository,
                catalogTypeRepository,
                catalogBrandRepository,
                catalogIntegrationService);

        Field picBaseUrlField = CatalogService.class.getDeclaredField("picBaseUrl");
        picBaseUrlField.setAccessible(true);
        picBaseUrlField.set(catalogService, "https://example.com/images/[0]");
    }

    @Test
    void getCatalogItemsByIds_returnsCatalogItemsWithResolvedPictureUris() {
        CatalogItem item = new CatalogItem();
        item.setId(1);
        item.setPictureFileName("shirt.png");

        when(catalogItemRepository.findAllById(List.of(1, 2))).thenReturn(List.of(item));

        List<CatalogItem> result = catalogService.getCatalogItemsByIds("1,2");

        assertEquals(1, result.size());
        assertEquals("https://example.com/images/shirt.png", result.get(0).getPictureUri());
        verify(catalogItemRepository).findAllById(List.of(1, 2));
    }

    @Test
    void getCatalogItemsByIds_withInvalidNumericList_returnsEmptyList() {
        List<CatalogItem> result = catalogService.getCatalogItemsByIds("1,not-a-number");

        assertTrue(result.isEmpty());
        verifyNoInteractions(catalogItemRepository);
    }

    @Test
    void updateProduct_whenPriceChanges_persistsAndQueuesEvent() {
        CatalogItem existingItem = new CatalogItem();
        existingItem.setId(10);
        existingItem.setPrice(new BigDecimal("20.00"));

        CatalogItem requestedItem = new CatalogItem();
        requestedItem.setId(10);
        requestedItem.setPrice(new BigDecimal("25.00"));

        when(catalogItemRepository.findById(10)).thenReturn(Optional.of(existingItem));

        boolean result = catalogService.updateProduct(requestedItem);

        assertTrue(result);
        verify(catalogIntegrationService).saveEventAndCatalogChanges(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    void updateProduct_whenPriceDoesNotChange_savesItemWithoutEvent() {
        CatalogItem existingItem = new CatalogItem();
        existingItem.setId(7);
        existingItem.setPrice(new BigDecimal("15.00"));

        CatalogItem requestedItem = new CatalogItem();
        requestedItem.setId(7);
        requestedItem.setPrice(new BigDecimal("15.00"));

        when(catalogItemRepository.findById(7)).thenReturn(Optional.of(existingItem));

        boolean result = catalogService.updateProduct(requestedItem);

        assertTrue(result);
        verify(catalogItemRepository).save(requestedItem);
        verifyNoInteractions(catalogIntegrationService);
    }

    @Test
    void updateProduct_whenItemDoesNotExist_returnsFalse() {
        CatalogItem requestedItem = new CatalogItem();
        requestedItem.setId(42);
        requestedItem.setPrice(new BigDecimal("99.00"));

        when(catalogItemRepository.findById(42)).thenReturn(Optional.empty());

        boolean result = catalogService.updateProduct(requestedItem);

        assertFalse(result);
    }
}
