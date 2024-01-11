package com.eshoponcontainers.orderapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.IBuyerRepository;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.PaymentMethod;

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
        return buyer.toString();
    }
    
}
