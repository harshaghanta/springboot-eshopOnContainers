package com.eshoponcontainers.orderapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.orderapi.application.commands.CancelOrderCommand;
import com.eshoponcontainers.orderapi.application.commands.CreateOrderDraftCommand;
import com.eshoponcontainers.orderapi.application.commands.ShipOrderCommand;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestBody CancelOrderCommand command, @RequestHeader(name = "x-requestid") String requestId) {        
        
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> shipOrder(@RequestBody ShipOrderCommand command, @RequestHeader(name = "x-requestid") String requestId) {        
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Void>  getMethodName(@PathVariable Integer orderId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<Void> getOrders(@RequestParam String param) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cardtypes")
    public ResponseEntity<Void> getCardTypes() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/draft")
    public ResponseEntity<Void> createOrderDraftFromBasketData(@RequestBody CreateOrderDraftCommand command) {
        return ResponseEntity.ok().build();
    }
}
