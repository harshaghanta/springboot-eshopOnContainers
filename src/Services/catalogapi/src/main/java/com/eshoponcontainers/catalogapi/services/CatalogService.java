package com.eshoponcontainers.catalogapi.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.catalogapi.controllers.viewmodels.PaginatedItemViewModel;
import com.eshoponcontainers.catalogapi.entities.CatalogBrand;
import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.entities.CatalogType;
import com.eshoponcontainers.catalogapi.integrationevents.events.ProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.catalogapi.repositories.CatalogBrandRepository;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.catalogapi.repositories.CatalogTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

	private final CatalogItemRepository catalogItemRepository;
	private final CatalogTypeRepository catalogTypeRepository;
	private final CatalogBrandRepository catalogBrandRepository;
	private final CatalogIntegrationService catalogIntegrationService;

	@Value("${picBaseUrl}")
	private String picBaseUrl;

	@Transactional(readOnly = true)
	public List<CatalogItem> getCatalogItemsByIds(String ids) {
		List<Integer> itemIds = new ArrayList<>();
		String[] catalogIds = ids.split(",");

		for (String catalogId : catalogIds) {
			try {
				Integer id = Integer.parseInt(catalogId);
				itemIds.add(id);
			} catch (NumberFormatException _) {
				return new ArrayList<>();
			}
		}

		return changeUrlPlaceHolder(catalogItemRepository.findAllById(itemIds));
	}

	@Transactional(readOnly = true)
	public PaginatedItemViewModel<CatalogItem> getItems(Integer pageIndex, Integer pageSize) {
		Page<CatalogItem> pageResults = catalogItemRepository
				.findAll(PageRequest.of(pageIndex, pageSize, Sort.by("name").ascending()));
		List<CatalogItem> catalogItems = changeUrlPlaceHolder(pageResults.getContent());
		return new PaginatedItemViewModel<>(pageIndex, pageSize, (int) pageResults.getTotalElements(), catalogItems);
	}

	@Transactional(readOnly = true)
	public Optional<CatalogItem> getItemById(Integer id) {
		return catalogItemRepository.findById(id).map(this::changeUrlPlaceHolder);
	}

	@Transactional(readOnly = true)
	public PaginatedItemViewModel<CatalogItem> getItemsByName(String name, Integer pageIndex, Integer pageSize) {
		Page<CatalogItem> items = catalogItemRepository.findByNameStartsWith(name,
				PageRequest.of(pageIndex, pageSize, Sort.by("id").ascending()));
		List<CatalogItem> catalogItems = changeUrlPlaceHolder(items.getContent());
		return new PaginatedItemViewModel<>(pageIndex, pageSize, (int) items.getTotalElements(), catalogItems);
	}

	@Transactional(readOnly = true)
	public PaginatedItemViewModel<CatalogItem> getItemsByCatalogTypeAndBrand(Integer catalogTypeId,
			Integer catalogBrandId, Integer pageIndex, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("id").ascending());
		Page<CatalogItem> catalogItems = catalogBrandId == null
				? catalogItemRepository.findByCatalogType_Id(catalogTypeId, pageRequest)
				: catalogItemRepository.findByCatalogType_IdAndCatalogBrand_Id(catalogTypeId, catalogBrandId,
						pageRequest);

		List<CatalogItem> items = changeUrlPlaceHolder(catalogItems.getContent());
		return new PaginatedItemViewModel<>(pageIndex, pageSize, (int) catalogItems.getTotalElements(), items);
	}

	@Transactional(readOnly = true)
	public PaginatedItemViewModel<CatalogItem> getItemsByBrand(Integer catalogBrandId, Integer pageIndex,
			Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("id").ascending());
		Page<CatalogItem> catalogItems = catalogBrandId == null
				? catalogItemRepository.findAll(pageRequest)
				: catalogItemRepository.findByCatalogBrand_Id(catalogBrandId, pageRequest);

		List<CatalogItem> items = changeUrlPlaceHolder(catalogItems.getContent());
		return new PaginatedItemViewModel<>(pageIndex, pageSize, (int) catalogItems.getTotalElements(), items);
	}

	@Transactional(readOnly = true)
	public List<CatalogType> getAllCatalogTypes() {
		return catalogTypeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<CatalogBrand> getAllCatalogBrands() {
		return catalogBrandRepository.findAll();
	}

	@Transactional
	public boolean updateProduct(CatalogItem requestedItem) {
        log.info("Updating product with id: {}", requestedItem.getId());
		Optional<CatalogItem> catalogItem = catalogItemRepository.findById(requestedItem.getId());
		if (catalogItem.isEmpty()) {
            log.warn("Item with id: {} not found", requestedItem.getId());
			return false;
		}

		BigDecimal oldPrice = catalogItem.get().getPrice();
		BigDecimal newPrice = requestedItem.getPrice();
		boolean raisePriceChangedEvent = oldPrice.compareTo(newPrice) != 0;

		if (raisePriceChangedEvent) {
			ProductPriceChangedIntegrationEvent productPriceChangedIntegrationEvent = new ProductPriceChangedIntegrationEvent(
					requestedItem.getId(), requestedItem.getPrice(), oldPrice);
			catalogIntegrationService.saveEventAndCatalogChanges(productPriceChangedIntegrationEvent,
					List.of(requestedItem));
			log.info("Persisted updated catalog item and queued ProductPriceChangedIntegrationEvent for item {}",
					requestedItem.getId());
			return true;
		}

		catalogItemRepository.save(requestedItem);
		log.info("Persisted catalog item {} without price change event", requestedItem.getId());
		return true;
	}

	@Transactional
	public CatalogItem createProduct(CatalogItem catalogItem) {
		return catalogItemRepository.save(catalogItem);
	}

	@Transactional
	public void deleteProduct(Integer id) {
		catalogItemRepository.deleteById(id);
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
