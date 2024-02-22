package com.eshoponcontainers.orderapi.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.eshoponcontainers.aggregatesModel.buyerAggregate.CardType;
import com.eshoponcontainers.orderapi.application.commands.CancelOrderCommand;
import com.eshoponcontainers.orderapi.application.commands.CreateOrderCommand;
import com.eshoponcontainers.orderapi.application.commands.CreateOrderDraftCommand;
import com.eshoponcontainers.orderapi.application.commands.ShipOrderCommand;
import com.eshoponcontainers.orderapi.application.queries.OrderQueries;
import com.eshoponcontainers.orderapi.application.viewModels.CardType;
import com.eshoponcontainers.orderapi.application.viewModels.Order;
import com.eshoponcontainers.orderapi.application.viewModels.OrderDraftDTO;
import com.eshoponcontainers.orderapi.application.viewModels.OrderSummary;
import com.eshoponcontainers.orderapi.services.IdentityService;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrdersController {

    private final Pipeline pipeline;
    private final OrderQueries orderQueries;
    private final IdentityService identityService;

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestBody CancelOrderCommand command, @RequestHeader(name = "x-requestid") String requestId) {        
        
        Boolean result = pipeline.send(command);

        log.info("---Sending command: {} - {}: {}, ({}})", CancelOrderCommand.class.getSimpleName(), "OrderNumber", command.getOrderNumber(), command);

        if(!result)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }

    @PutMapping("/ship")
    public ResponseEntity<Void> shipOrder(@RequestBody ShipOrderCommand command, @RequestHeader(name = "x-requestid") String requestId) {        
        
        Boolean result = pipeline.send(command);

        log.info("---Sending command: {} - {}: {}, ({}})", CancelOrderCommand.class.getSimpleName(), "OrderNumber", command.getOrderNumber(), command);

        if(!result)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order>  getOrder(@PathVariable Integer orderId) {

        Order order = orderQueries.getOrder(orderId);
        if(order == null)
            return ResponseEntity.notFound().build();
            
        return ResponseEntity.ok().body(order);
    }

    @GetMapping()
    public ResponseEntity<List<OrderSummary>> getOrders() {        
        String strUserId = identityService.getUserId();
        UUID userId  =  UUID.fromString(strUserId);
        List<OrderSummary> orders = orderQueries.getOrdersFromUser(userId);
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/cardtypes")
    public ResponseEntity<List<CardType>> getCardTypes() {
        List<CardType> cardTypes = orderQueries.getCardTypes();
        return ResponseEntity.ok().body(cardTypes);
    }

    @PostMapping("/draft")
    public ResponseEntity<OrderDraftDTO> createOrderDraftFromBasketData(@RequestBody CreateOrderDraftCommand command) {

        log.info("---Sending command: {} - {}: {}, ({}})", CancelOrderCommand.class.getSimpleName(), "BuyerId", command.getBuyerId(), command);

        OrderDraftDTO orderDraft = pipeline.send(command);

        return ResponseEntity.ok().body(orderDraft);
    }

    // @PostMapping("/createorder")
    // public String CreateOrder(@RequestBody CreateOrderCommand command) {
    //    Boolean status = pipeline.send(command);
        
    //     return "successfull";
    // }
    
}
