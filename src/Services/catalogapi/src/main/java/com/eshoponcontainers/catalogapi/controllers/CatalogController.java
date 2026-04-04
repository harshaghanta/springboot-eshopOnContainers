package com.eshoponcontainers.catalogapi.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
import com.eshoponcontainers.catalogapi.services.CatalogService;

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

    private final CatalogService catalogService;

    @GetMapping("/items")
    @Operation(summary = "Get all/specified catalog items", description = "Get all/specified catalog items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CatalogItem.class)))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "200", description = "PaginatedViewModel", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedItemViewModel.class)))
    })
    public ResponseEntity<Object> getItems(
            @RequestParam(name = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, name = "ids") String ids) {

        log.info("Entered method getitems");

        if (!(ids == null || ids.isEmpty())) {

            List<CatalogItem> catalogItems = catalogService.getCatalogItemsByIds(ids);
            if (catalogItems.isEmpty()) {
                var message = "ids value invalid. Must be comma-separated list of numbers";
                log.warn(message);
                return ResponseEntity.badRequest().body(message);
            }

            return ResponseEntity.ok(catalogItems);
        }

        return ResponseEntity.ok(catalogService.getItems(pageIndex, pageSize));

    }

    @GetMapping("/items/{id}")
    public ResponseEntity<CatalogItem> getItemsById(@PathVariable("id") Integer id) {

        if (id != null && id <= 0)
            return ResponseEntity.badRequest().build();

        Optional<CatalogItem> item = catalogService.getItemById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }

        log.error("Item with id: {} not found",id);
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/items/withname/{name}")
    public ResponseEntity<PaginatedItemViewModel<CatalogItem>> getItemsByName(
            @PathVariable(name = "name") @Min(1) String name,
            @RequestParam(name = "pageIndex", defaultValue = "0") Integer pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(catalogService.getItemsByName(name, pageIndex, pageSize));

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
        return ResponseEntity.ok(catalogService.getItemsByCatalogTypeAndBrand(catalogTypeId, catalogBrandId,
            pageIndex, pageSize));
    }

    @GetMapping("/items/type/all/brand/{catalogBrandId}")
    public ResponseEntity<PaginatedItemViewModel<CatalogItem>> getItemsByBrand(
            @PathVariable(name = "catalogBrandId", required = false) Integer catalogBrandId,
            @RequestParam(name = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(catalogService.getItemsByBrand(catalogBrandId, pageIndex, pageSize));
    }

    @GetMapping("/catalogTypes")
    public ResponseEntity<List<CatalogType>> getAllCatalogTypes() {
        return ResponseEntity.ok(catalogService.getAllCatalogTypes());
    }

    @GetMapping("/catalogBrands")
    public ResponseEntity<List<CatalogBrand>> getAllCatalogBrands() {
        return ResponseEntity.ok(catalogService.getAllCatalogBrands());
    }

    @PutMapping("/items")
    public ResponseEntity<Object> updateProduct(@RequestBody CatalogItem requestedItem) {

        if (!catalogService.updateProduct(requestedItem)) {
            var message = "Item with id " + requestedItem.getId() + " not found.";
            log.error(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }

        URI location = URI.create("/items/" + requestedItem.getId());
        return ResponseEntity.created(location).build();

    }

    @PostMapping("/items")
    public ResponseEntity<CatalogItem> createProduct(@RequestBody CatalogItem catalogItem) {
        CatalogItem savedItem = catalogService.createProduct(catalogItem);
        URI location = URI.create("/items/" + savedItem.getId());
        return ResponseEntity.created(location).body(savedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        catalogService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
