package com.eshoponcontainers.orderapi.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.IBuyerRepository;
import com.eshoponcontainers.orderapi.application.viewModels.BuyerDto;


@RestController
@RequestMapping("/api/v1/buyers")
public class BuyerController {

    private final IBuyerRepository buyerRepository;

    public BuyerController(IBuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    @PostMapping("/addbuyer")    
    public String addBuyer(@RequestBody BuyerDto buyerDto) {

        Buyer buyer = new Buyer( buyerDto.getUserId(), buyerDto.getName());

        buyer.verifyOrAddPaymentMethod(buyerDto.getCardTypeId(), "Payment Method on " + buyerDto.getName(), buyerDto.getCardNumber(),
                buyerDto.getSecurityNumber(), buyerDto.getCardHolderName(), buyerDto.getExpiration(),
                1);
        
        buyerRepository.add(buyer);
        
        return buyer.getIdentityUUID();
    }   
  
}
