package com.eshoponcontainers.orderapi.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.IBuyerRepository;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.PaymentMethod;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;

import jakarta.persistence.EntityManager;



@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IBuyerRepository buyerRepository;

    @Autowired
    private EntityManager entityManager;

    @GetMapping()
    public String test() {
        Buyer buyer = buyerRepository.findById("1");
        PaymentMethod paymentMethod = entityManager.find(PaymentMethod.class, 1);
        Order order = entityManager.find(Order.class, 21);
        
        UUID uuid = UUID.fromString("8E3D5A36-266E-4C86-A4AB-01229EB383F1");
        // IntegrationEventLogEntry eventLogEntry = entityManager.find(IntegrationEventLogEntry.class, uuid);
        return "Test";
    }
    
}
