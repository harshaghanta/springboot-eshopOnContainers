package com.eshoponcontainers.orderapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.IBuyerRepository;


@RestController("/test")
public class TestController {

    @Autowired
    private IBuyerRepository buyerRepository;

    @GetMapping()
    public Buyer test() {
        return buyerRepository.findById("1");
    }
    
}
