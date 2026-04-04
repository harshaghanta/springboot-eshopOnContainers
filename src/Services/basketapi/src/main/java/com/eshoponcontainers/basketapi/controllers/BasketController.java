package com.eshoponcontainers.basketapi.controllers;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.basketapi.integrationevents.events.UserCheckoutAcceptedIntegrationEvent;
import com.eshoponcontainers.basketapi.model.BasketPriceChangedNotification;
import com.eshoponcontainers.basketapi.model.BasketItem;
import com.eshoponcontainers.basketapi.model.BasketCheckout;
import com.eshoponcontainers.basketapi.model.CustomerBasket;
import com.eshoponcontainers.basketapi.repositories.BasketNotificationStateRepository;
import com.eshoponcontainers.basketapi.repositories.RedisBasketDataRepository;
import com.eshoponcontainers.basketapi.services.IdentityService;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.proto.catalog.CatalogItemResponse;
import com.eshoponcontainers.proto.catalog.CatalogItemsRequest;
import com.eshoponcontainers.proto.catalog.PaginatedItemsResponse;
import com.eshoponcontainers.proto.catalog.CatalogGrpc.CatalogBlockingStub;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/basket")
@Slf4j
public class BasketController {

    private static final long CATALOG_GRPC_DEADLINE_SECONDS = 2;

    private final RedisBasketDataRepository basketDataRepository;
    private final BasketNotificationStateRepository notificationStateRepository;
    private final EventBus eventBus;
    private final IdentityService identityService;
    private final CatalogBlockingStub catalogBlockingStub;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerBasket> getBasket(@PathVariable String id, Principal principal) {
        CustomerBasket basket = basketDataRepository.getBasket(id);

        if (basket == null) {
            basket = new CustomerBasket(id);
        }

        notificationStateRepository.saveUserContext(identityService.getUserId(), identityService.getUsername());
        enrichBasket(basket);
        applyPriceNotifications(basket);

        return ResponseEntity.ok(basket);
    }

    @PostMapping({ "/", "" })
    public ResponseEntity<CustomerBasket> updateBasket(@RequestBody CustomerBasket basket) {
        log.info("Received update basket request : {} for the basket with id: {}", basket, basket.getBuyerId());
        CustomerBasket updatedBasket = basketDataRepository.updateBasket(basket, identityService.getUsername());
        enrichBasket(updatedBasket);
        applyPriceNotifications(updatedBasket);
        log.info("Sending updated Basket: {} to client", updatedBasket);
        return ResponseEntity.ok(updatedBasket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBasket(@PathVariable String id) {
        basketDataRepository.deleteBasket(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout(@RequestBody BasketCheckout basketCheckout,
            @RequestHeader(name = "x-requestid") String requestId, Principal principal) {
        UUID rquestUUID = null;
        String userId = identityService.getUserId();
        String userName = identityService.getUsername();
        log.info("Userid: {}, username: {}, RequestId: {}", userId, userName, requestId);
        try {
            rquestUUID = UUID.fromString(requestId);
            basketCheckout.setRequestId(rquestUUID);
        } catch (Exception _) {
            log.warn("Invalid request id received for checkout: {}", requestId);
        }

        CustomerBasket basket = basketDataRepository.getBasket(userId);
        if (basket == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!enrichBasket(basket)) {
            return ResponseEntity.badRequest().build();
        }

        UserCheckoutAcceptedIntegrationEvent event = new UserCheckoutAcceptedIntegrationEvent(userId, userName,
                basketCheckout.getCity(), basketCheckout.getStreet(), basketCheckout.getState(),
                basketCheckout.getCountry(), basketCheckout.getZipcode(), basketCheckout.getCardNumber(),
                basketCheckout.getCardHolderName(),
                basketCheckout.getCardExpiration(), basketCheckout.getCardSecurityNumber(),
                basketCheckout.getCardTypeId(), basketCheckout.getBuyer(),
                basketCheckout.getRequestId(), basket);

        log.info("RequestID: {}", event.getRequestId());

        eventBus.publish(event);

        return ResponseEntity.accepted().build();

    }

    private boolean enrichBasket(CustomerBasket basket) {
        if (basket == null || basket.getItems().isEmpty()) {
            return true;
        }

        CatalogItemsRequest request = CatalogItemsRequest.newBuilder()
                .setIds(basket.getItems().stream()
                        .map(item -> String.valueOf(item.getProductId()))
                        .distinct()
                        .collect(Collectors.joining(",")))
                .setPageSize(basket.getItems().size())
                .setPageIndex(0)
                .build();

        try {
            PaginatedItemsResponse itemsByIds = catalogBlockingStub
                .withDeadlineAfter(CATALOG_GRPC_DEADLINE_SECONDS, TimeUnit.SECONDS)
                .getItemsByIds(request);
            return enrichBasketItems(basket, itemsByIds);
        } catch (StatusRuntimeException exception) {
            log.warn("Catalog gRPC enrichment failed for buyer {} and products {}. Returning basket without enrichment.",
                basket.getBuyerId(),
                basket.getItems().stream().map(BasketItem::getProductId).distinct().toList(),
                exception);
            return false;
        }
    }

    private boolean enrichBasketItems(CustomerBasket basket, PaginatedItemsResponse itemsByIds) {
        Map<Integer, CatalogItemResponse> itemsByProductId = itemsByIds.getDataList().stream()
                .collect(Collectors.toMap(CatalogItemResponse::getId, Function.identity(), (left, right) -> left));

        Set<Integer> missingProducts = new HashSet<>();

        for (BasketItem basketItem : basket.getItems()) {
            CatalogItemResponse catalogItem = itemsByProductId.get(basketItem.getProductId());
            if (catalogItem == null) {
                missingProducts.add(basketItem.getProductId());
                continue;
            }

            basketItem.setProductName(catalogItem.getName());
            basketItem.setUnitPrice(catalogItem.getPrice());
            basketItem.setPictureUrl(catalogItem.getPictureUri());
        }

        if (!missingProducts.isEmpty()) {
            log.warn("Catalog enrichment could not resolve products {} for buyer {}", missingProducts, basket.getBuyerId());
        }
        return missingProducts.isEmpty();
    }

    private void applyPriceNotifications(CustomerBasket basket) {
        if (basket == null || basket.getItems().isEmpty()) {
            return;
        }

        Map<Integer, BasketPriceChangedNotification> notifications = notificationStateRepository
                .getNotifications(basket.getBuyerId());
        for (BasketItem basketItem : basket.getItems()) {
            BasketPriceChangedNotification notification = notifications.get(basketItem.getProductId());
            if (notification == null || Double.compare(notification.getCurrentPrice(), basketItem.getUnitPrice()) != 0) {
                basketItem.setOldUnitPrice(0);
                continue;
            }
            basketItem.setOldUnitPrice(notification.getPreviousPrice());
        }
    }

}
