package com.eshoponcontainers.basketapi.controllers;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.UUID;

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
import com.eshoponcontainers.basketapi.model.BasketCheckout;
import com.eshoponcontainers.basketapi.model.CustomerBasket;
import com.eshoponcontainers.basketapi.repositories.RedisBasketDataRepository;
import com.eshoponcontainers.basketapi.services.IdentityService;
import com.eshoponcontainers.eventbus.abstractions.EventBus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/basket")
@Slf4j
public class BasketController {

    private final RedisBasketDataRepository basketDataRepository;
    private final EventBus eventBus;
    private final IdentityService identityService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerBasket> getBasket(@PathVariable String id, Principal principal) {
        CustomerBasket basket = basketDataRepository.getBasket(id);
        if (basket == null)
            basket = new CustomerBasket(id);

        return ResponseEntity.ok(basket);
    }

    @PostMapping()
    public ResponseEntity<CustomerBasket> updateBasket(@RequestBody CustomerBasket basket) {
        log.info("Received update basket request : {} for the basket with id: {}", basket,  basket.getBuyerId());
        CustomerBasket updatedBasket = basketDataRepository.updateBasket(basket);
        log.info("Sending updated Basket: {} to client",  updatedBasket);
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
        } catch (Exception e) {
        }

        CustomerBasket basket = basketDataRepository.getBasket(userId);
        if (basket == null)
            return ResponseEntity.badRequest().build();

        UserCheckoutAcceptedIntegrationEvent event = new UserCheckoutAcceptedIntegrationEvent(userId, userName,
                basketCheckout.getCity(), basketCheckout.getStreet(), basketCheckout.getState(),
                basketCheckout.getCountry(), basketCheckout.getZipcode(), basketCheckout.getCardNumber(),
                basketCheckout.getCardHolderName(),
                basketCheckout.getCardExpiration(), basketCheckout.getCardSecurityNumber(),
                basketCheckout.getCardTypeId(), basketCheckout.getBuyer(),
                basketCheckout.getRequestId(), basket);

        log.info("RequestID: {}", event.getRequestId());

        try {
            eventBus.publish(event);
        } catch (Exception e) {
            log.error(MessageFormat.format("ERROR Publishing Integration event: {0} from {1}", event.getId(), "Basket"),
                    e);
        }

        return ResponseEntity.accepted().build();

    }

}
