package com.eshoponcontainers.catalogapi.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.Min;

import com.eshoponcontainers.catalogapi.controllers.viewmodels.PaginatedItemViewModel;
import com.eshoponcontainers.catalogapi.entities.CatalogBrand;
import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.entities.CatalogType;
import com.eshoponcontainers.catalogapi.integrationevents.events.ProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.catalogapi.repositories.CatalogBrandRepository;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.catalogapi.repositories.CatalogTypeRepository;
import com.eshoponcontainers.catalogapi.services.CatalogIntegrationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/catalog")
public class CatalogController {
    
    private final CatalogItemRepository catalogItemRepository;
    private final CatalogTypeRepository catalogTypeRepository;
    private final CatalogBrandRepository catalogBrandRepository;
    private final CatalogIntegrationService catalogIntegrationService;
    
    @GetMapping("/items")
    public ResponseEntity<?> getItems(@RequestParam( name = "pageIndex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(required = false, defaultValue = "10") Integer pageSize, @RequestParam (required = false, name = "ids" ) String ids) {
        
        if(pageIndex < 0 || pageSize < 0) {
            throw new InvalidParameterException();
        }

        if(ids == null) {
            Page<CatalogItem> pageResults = catalogItemRepository.findAll(PageRequest.of(pageIndex, pageSize, Sort.by("id").ascending()));
            PaginatedItemViewModel<CatalogItem> paginatedItemViewModel = new PaginatedItemViewModel<CatalogItem>(pageIndex, pageSize, (int) pageResults.getTotalElements(), pageResults.getContent());
            return ResponseEntity.ok(paginatedItemViewModel);
        }
        else 
        {
            List<Integer> itemIds = Stream.of(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<CatalogItem> items = catalogItemRepository.findAllById(itemIds);
            return ResponseEntity.ok(items);
        }
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<CatalogItem> getItemsById(@PathVariable("id") Integer id) {
        Optional<CatalogItem> item = catalogItemRepository.findById(id);
        if(item.isPresent())
            return ResponseEntity.ok(item.get());

        return ResponseEntity.notFound().build();
        
    }

    @GetMapping("/items/withname/{name}")
    public ResponseEntity<PaginatedItemViewModel<CatalogItem>> getItemsByName(@PathVariable(name =  "name") @Min(1) String name, 
        @RequestParam(name =  "pageIndex", defaultValue = "0")Integer pageIndex, 
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) 
    {
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("id").ascending());    
        Page<CatalogItem> items = catalogItemRepository.findByNameStartsWith(name, pageRequest);
        
        PaginatedItemViewModel<CatalogItem> paginatedItemViewModel = new PaginatedItemViewModel<CatalogItem>(pageIndex, pageSize, (int) items.getTotalElements(), items.getContent());
        return ResponseEntity.ok(paginatedItemViewModel);
        
    }

    //TODO: NEED TO VERIFY WHY API IS NOT GETTING HIT WHILE SKIPPING catalogBrandId
    @GetMapping("/items/type/{catalogTypeId}/brand/{catalogBrandId}")
    public ResponseEntity<PaginatedItemViewModel<CatalogItem>> getItemsByCatalogTypeAndBrand(@PathVariable(name = "catalogTypeId", required = true) Integer catalogTypeId,
        @PathVariable(name = "catalogBrandId", required = false) Integer catalogBrandId,
        @RequestParam(name = "pageIndex", required = false, defaultValue = "0")Integer pageIndex,
        @RequestParam(name = "pageSize", required = false,  defaultValue = "10") Integer pageSize)
    {
        Page<CatalogItem> catalogItems = null;
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("id").ascending());
        if(catalogBrandId == null) {
            catalogItems = catalogItemRepository.findByCatalogType_Id(catalogTypeId, pageRequest);
        }
        else {
            catalogItems = catalogItemRepository.findByCatalogType_IdAndCatalogBrand_Id(catalogTypeId,catalogBrandId, pageRequest);
        }

        PaginatedItemViewModel<CatalogItem> paginatedItemViewModel = new PaginatedItemViewModel<>(pageIndex, pageSize, (int) catalogItems.getTotalElements(), catalogItems.getContent());
        return ResponseEntity.ok(paginatedItemViewModel);
    }

    @GetMapping("/items/type/all/brand/{catalogBrandId}")
    public ResponseEntity<PaginatedItemViewModel<CatalogItem>> getItemsByBrand(@PathVariable(name = "catalogBrandId", required = false)Integer catalogBrandId,
        @RequestParam(name = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
        @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize)
    {
        Page<CatalogItem> catalogItems = null;
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("id").ascending());
        if(catalogBrandId == null) {
            catalogItems = catalogItemRepository.findAll(pageRequest);
        }
        else {
            catalogItems = catalogItemRepository.findByCatalogBrand_Id(catalogBrandId, pageRequest);
        }
        PaginatedItemViewModel<CatalogItem> paginatedItemViewModel = new PaginatedItemViewModel<>(pageIndex, pageSize, (int)catalogItems.getTotalElements(), catalogItems.getContent());
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
        if(!catalogItem.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item with id " + requestedItem.getId() +" not found.");
        }

        BigDecimal oldPrice = catalogItem.get().getPrice();
        BigDecimal newPrice = requestedItem.getPrice();

        boolean raisePriceChangedEvent = oldPrice != newPrice;

        if(raisePriceChangedEvent) {
            ProductPriceChangedIntegrationEvent productPriceChangedIntegrationEvent = new ProductPriceChangedIntegrationEvent(requestedItem.getId(), requestedItem.getPrice(), oldPrice);
            catalogIntegrationService.saveEventAndCatalogChanges(productPriceChangedIntegrationEvent, requestedItem);
            catalogIntegrationService.publishThroughEventBus(productPriceChangedIntegrationEvent);
        }
        else {
            catalogItemRepository.save(requestedItem);
        }

        URI location = URI.create("/items/"+ requestedItem.getId());
        return ResponseEntity.created(location).build();
      
    }
    
}
