package com.eshoponcontainers.catalogapi.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.proto.catalog.CatalogBrand;
import com.eshoponcontainers.proto.catalog.CatalogItemRequest;
import com.eshoponcontainers.proto.catalog.CatalogItemResponse;
import com.eshoponcontainers.proto.catalog.CatalogItemsRequest;
import com.eshoponcontainers.proto.catalog.CatalogType;
import com.eshoponcontainers.proto.catalog.PaginatedItemsResponse;
import com.eshoponcontainers.proto.catalog.CatalogGrpc.CatalogImplBase;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class CatalogGrpcService extends CatalogImplBase {

    private final CatalogItemRepository catalogItemRepository;

    @Value("${picBaseUrl}")
    private String picBaseUrl;

    @Override
    public void getItemById(CatalogItemRequest request, StreamObserver<CatalogItemResponse> responseObserver) {
        int id = request.getId();
        log.info("gRPC getItemById called with id: {}", id);

        if (id <= 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Invalid id").asRuntimeException());
            return;
        }

        Optional<CatalogItem> item = catalogItemRepository.findById(id);
        if (item.isEmpty()) {
            log.error("Item with id: {} not found", id);
            responseObserver.onError(Status.NOT_FOUND.withDescription("Item not found").asRuntimeException());
            return;
        }

        CatalogItem catalogItem = item.get();
        catalogItem.setPictureUri(picBaseUrl.replace("[0]", catalogItem.getPictureFileName()));

        responseObserver.onNext(mapToGrpcResponse(catalogItem));
        responseObserver.onCompleted();
    }

    @Override
    public void getItemsByIds(CatalogItemsRequest request, StreamObserver<PaginatedItemsResponse> responseObserver) {
        String ids = request.getIds();
        int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 10;
        int pageIndex = request.getPageIndex();

        log.info("gRPC getItemsByIds called with ids: {}, pageSize: {}, pageIndex: {}", ids, pageSize, pageIndex);

        PaginatedItemsResponse.Builder responseBuilder = PaginatedItemsResponse.newBuilder()
                .setPageIndex(pageIndex)
                .setPageSize(pageSize);

        if (ids != null && !ids.isEmpty()) {
            List<CatalogItem> catalogItems = getCatalogItems(ids);
            if (catalogItems.isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("ids value invalid. Must be comma-separated list of numbers")
                        .asRuntimeException());
                return;
            }

            for (CatalogItem item : catalogItems) {
                item.setPictureUri(picBaseUrl.replace("[0]", item.getPictureFileName()));
                responseBuilder.addData(mapToGrpcResponse(item));
            }
            responseBuilder.setCount(catalogItems.size());
        } else {
            Page<CatalogItem> pageResults = catalogItemRepository
                    .findAll(PageRequest.of(pageIndex, pageSize, Sort.by("name").ascending()));

            for (CatalogItem item : pageResults.getContent()) {
                item.setPictureUri(picBaseUrl.replace("[0]", item.getPictureFileName()));
                responseBuilder.addData(mapToGrpcResponse(item));
            }
            responseBuilder.setCount(pageResults.getTotalElements());
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private List<CatalogItem> getCatalogItems(String ids) {
        List<Integer> itemIds = new ArrayList<>();
        String[] catalogIds = ids.split(",");
        for (String catalogId : catalogIds) {
            try {
                itemIds.add(Integer.parseInt(catalogId.trim()));
            } catch (NumberFormatException _) {
                return new ArrayList<>();
            }
        }
        return catalogItemRepository.findAllById(itemIds);
    }

    private CatalogItemResponse mapToGrpcResponse(CatalogItem item) {
        CatalogItemResponse.Builder builder = CatalogItemResponse.newBuilder()
                .setId(item.getId())
                .setName(item.getName() != null ? item.getName() : "")
                .setDescription(item.getDescription() != null ? item.getDescription() : "")
                .setPrice(item.getPrice() != null ? item.getPrice().doubleValue() : 0.0)
                .setPictureFileName(item.getPictureFileName() != null ? item.getPictureFileName() : "")
                .setPictureUri(item.getPictureUri() != null ? item.getPictureUri() : "")
                .setAvailableStock(item.getAvailableStock())
                .setRestockThreshold(item.getRestockThreshold())
                .setMaxStockThreshold(item.getMaxStockThreshold())
                .setOnReorder(item.isOnReorder());

        if (item.getCatalogType() != null) {
            builder.setCatalogType(CatalogType.newBuilder()
                    .setId(item.getCatalogType().getId())
                    .setType(item.getCatalogType().getType() != null ? item.getCatalogType().getType() : "")
                    .build());
        }

        if (item.getCatalogBrand() != null) {
            builder.setCatalogBrand(CatalogBrand.newBuilder()
                    .setId(item.getCatalogBrand().getId())
                    .setName(item.getCatalogBrand().getBrand() != null ? item.getCatalogBrand().getBrand() : "")
                    .build());
        }

        return builder.build();
    }
}
