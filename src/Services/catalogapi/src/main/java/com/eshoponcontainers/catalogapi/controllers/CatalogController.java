package com.eshoponcontainers.catalogapi.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eshoponcontainers.catalogapi.controllers.viewmodels.PaginatedItemViewModel;
import com.eshoponcontainers.catalogapi.entities.CatalogBrand;
import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.entities.CatalogType;
import com.eshoponcontainers.catalogapi.integrationevents.events.ProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.catalogapi.repositories.CatalogBrandRepository;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.catalogapi.repositories.CatalogTypeRepository;
import com.eshoponcontainers.catalogapi.services.CatalogIntegrationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog")
@RestControllerAdvice
@Slf4j
public class CatalogController {

    private final CatalogItemRepository catalogItemRepository;
    private final CatalogTypeRepository catalogTypeRepository;
    private final CatalogBrandRepository catalogBrandRepository;
    private final CatalogIntegrationService catalogIntegrationService;

    @Value("${picBaseUrl}")
    private String picBaseUrl;

    @GetMapping("/items")
    @Operation(summary = "Get all/specified catalog items", description = "Get all/specified catalog items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CatalogItem.class)))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "200", description = "PaginatedViewModel", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedItemViewModel.class)))
    })
    public ResponseEntity<?> getItems(
            @RequestParam(name = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, name = "ids") String ids) {

        log.info("Entered method getitems");

        if (!(ids == null || ids.isEmpty())) {

            List<CatalogItem> catalogItems = getCatalogItems(ids);
            if (catalogItems.isEmpty()) {
                var message = "ids value invalid. Must be comma-separated list of numbers";
                log.warn(message);
                return ResponseEntity.badRequest().body(message);
            }

            changeUrlPlaceHolder(catalogItems);

            return ResponseEntity.ok(catalogItems);
        }

        Page<CatalogItem> pageResults = catalogItemRepository
                .findAll(PageRequest.of(pageIndex, pageSize, Sort.by("name").ascending()));
        changeUrlPlaceHolder(pageResults.getContent());
        PaginatedItemViewModel<CatalogItem> paginatedItemViewModel = new PaginatedItemViewModel<CatalogItem>(pageIndex,
                pageSize, (int) pageResults.getTotalElements(), pageResults.getContent());
        return ResponseEntity.ok(paginatedItemViewModel);

    }

    @GetMapping("/items/{id}")
    public ResponseEntity<CatalogItem> getItemsById(@PathVariable("id") Integer id) {

        if (id != null && id <= 0)
            return ResponseEntity.badRequest().build();

        Optional<CatalogItem> item = catalogItemRepository.findById(id);
        if (item.isPresent()) {
            CatalogItem catalogItem = changeUrlPlaceHolder(item.get());
            return ResponseEntity.ok(catalogItem);
        }

        log.error("Item with id: {} not found",id);
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/items/withname/{name}")
    public ResponseEntity<PaginatedItemViewModel<CatalogItem>> getItemsByName(
            @PathVariable(name = "name") @Min(1) String name,
            @RequestParam(name = "pageIndex", defaultValue = "0") Integer pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("id").ascending());
        Page<CatalogItem> items = catalogItemRepository.findByNameStartsWith(name, pageRequest);
        changeUrlPlaceHolder(items.getContent());

        PaginatedItemViewModel<CatalogItem> paginatedItemViewModel = new PaginatedItemViewModel<CatalogItem>(pageIndex,
                pageSize, (int) items.getTotalElements(), items.getContent());
        return ResponseEntity.ok(paginatedItemViewModel);

    }

    @GetMapping("/items/type/{catalogTypeId}/brand/")
    public ResponseEntity<PaginatedItemViewModel<CatalogItem>> getItemsByCatalogType(
            @PathVariable("catalogTypeId") Integer catalogTypeId,
            @RequestParam(name = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return getItemsByCatalogTypeAndBrand(catalogTypeId, null, pageIndex, pageSize);
    }

    @GetMapping("/items/type/{catalogTypeId}/brand/{catalogBrandId}")
    public ResponseEntity<PaginatedItemViewModel<CatalogItem>> getItemsByCatalogTypeAndBrand(
            @PathVariable("catalogTypeId") Integer catalogTypeId,
            @PathVariable(name = "catalogBrandId", required = true) Integer catalogBrandId,
            @RequestParam(name = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Page<CatalogItem> catalogItems = null;
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("id").ascending());
        if (catalogBrandId == null) {
            catalogItems = catalogItemRepository.findByCatalogType_Id(catalogTypeId, pageRequest);
        } else {
            catalogItems = catalogItemRepository.findByCatalogType_IdAndCatalogBrand_Id(catalogTypeId, catalogBrandId,
                    pageRequest);
        }

        changeUrlPlaceHolder(catalogItems.getContent());
        PaginatedItemViewModel<CatalogItem> paginatedItemViewModel = new PaginatedItemViewModel<>(pageIndex, pageSize,
                (int) catalogItems.getTotalElements(), catalogItems.getContent());
        return ResponseEntity.ok(paginatedItemViewModel);
    }

    @GetMapping("/items/type/all/brand/{catalogBrandId}")
    public ResponseEntity<PaginatedItemViewModel<CatalogItem>> getItemsByBrand(
            @PathVariable(name = "catalogBrandId", required = false) Integer catalogBrandId,
            @RequestParam(name = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Page<CatalogItem> catalogItems = null;
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("id").ascending());
        if (catalogBrandId == null) {
            catalogItems = catalogItemRepository.findAll(pageRequest);
        } else {
            catalogItems = catalogItemRepository.findByCatalogBrand_Id(catalogBrandId, pageRequest);
        }
        changeUrlPlaceHolder(catalogItems.getContent());
        PaginatedItemViewModel<CatalogItem> paginatedItemViewModel = new PaginatedItemViewModel<>(pageIndex, pageSize,
                (int) catalogItems.getTotalElements(), catalogItems.getContent());
        return ResponseEntity.ok(paginatedItemViewModel);
    }

    @GetMapping("/catalogTypes")
    public ResponseEntity<List<CatalogType>> getAllCatalogTypes() {
        List<CatalogType> catalogTypes = catalogTypeRepository.findAll();
        return ResponseEntity.ok(catalogTypes);
    }

    @GetMapping("/catalogBrands")
    public ResponseEntity<List<CatalogBrand>> getAllCatalogBrands() {
        return ResponseEntity.ok(catalogBrandRepository.findAll());
    }

    @PutMapping("/items")
    public ResponseEntity<?> updateProduct(@RequestBody CatalogItem requestedItem) {

        Optional<CatalogItem> catalogItem = catalogItemRepository.findById(requestedItem.getId());
        if (!catalogItem.isPresent()) {
            var message = "Item with id " + requestedItem.getId() + " not found.";
            log.error(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }

        BigDecimal oldPrice = catalogItem.get().getPrice();
        BigDecimal newPrice = requestedItem.getPrice();

        boolean raisePriceChangedEvent = oldPrice != newPrice;
        List<CatalogItem> catitems = new ArrayList<>();
        catitems.add(requestedItem);

        if (raisePriceChangedEvent) {
            ProductPriceChangedIntegrationEvent productPriceChangedIntegrationEvent = new ProductPriceChangedIntegrationEvent(
                    requestedItem.getId(), requestedItem.getPrice(), oldPrice);
            catalogIntegrationService.saveEventAndCatalogChanges(productPriceChangedIntegrationEvent, catitems);
            catalogIntegrationService.publishThroughEventBus(productPriceChangedIntegrationEvent);
        } else {
            catalogItemRepository.save(requestedItem);
        }

        URI location = URI.create("/items/" + requestedItem.getId());
        return ResponseEntity.created(location).build();

    }

    @PostMapping("/items")
    public ResponseEntity<CatalogItem> createProduct(@RequestBody CatalogItem catalogItem) {
        CatalogItem savedItem = catalogItemRepository.save(catalogItem);
        URI location = URI.create("/items/" + savedItem.getId());
        return ResponseEntity.created(location).body(savedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        catalogItemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private List<CatalogItem> getCatalogItems(String ids) {

        List<Integer> itemIds = new ArrayList<>();

        String[] catalogIds = ids.split(",");

        for (String catalogId : catalogIds) {
            try {
                Integer id = Integer.parseInt(catalogId);
                itemIds.add(id);
            } catch (NumberFormatException e) {
                return new ArrayList<>();
            }
        }

        return catalogItemRepository.findAllById(itemIds);
    }

    private List<CatalogItem> changeUrlPlaceHolder(List<CatalogItem> catalogItems) {

        for (CatalogItem catalogItem : catalogItems) {
            changeUrlPlaceHolder(catalogItem);
        }
        return catalogItems;
    }

    private CatalogItem changeUrlPlaceHolder(CatalogItem catalogItem) {
        catalogItem.setPictureUri(picBaseUrl.replace("[0]", catalogItem.getPictureFileName()));
        return catalogItem;
    }

}
