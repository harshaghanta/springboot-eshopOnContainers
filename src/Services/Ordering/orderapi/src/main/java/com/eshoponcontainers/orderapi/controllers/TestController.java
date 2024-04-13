package com.eshoponcontainers.orderapi.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.IBuyerRepository;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.PaymentMethod;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.entities.IntegrationEventLogEntry;
import com.eshoponcontainers.services.IntegrationEventLogService;

import jakarta.persistence.EntityManager;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IBuyerRepository buyerRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private IntegrationEventLogService eventlogService;

    @GetMapping()
    public String test() {
        Buyer buyer = buyerRepository.findById("1");
        PaymentMethod paymentMethod = entityManager.find(PaymentMethod.class, 1);
        Order order = entityManager.find(Order.class, 21);

        UUID uuid = UUID.fromString("96544e9e-4a4c-4cb1-bd82-d3fcbc83b496");
        // IntegrationEventLogEntry eventLogEntry =
        // entityManager.find(IntegrationEventLogEntry.class, uuid);

        List<IntegrationEventLogEntry> eventLogsPendingToPublish = eventlogService
                .retrieveEventLogsPendingToPublish(uuid);
        return "Test";
    }

}
