package com.eshoponcontainers.orderapi.application.commands;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.eshoponcontainers.orderapi.application.viewModels.BasketItem;
import com.eshoponcontainers.orderapi.application.viewModels.OrderItemDTO;

import an.awesome.pipelinr.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateOrderCommand implements Command<Boolean> {

    private List<OrderItemDTO> orderItems;
    private String userId;
    private String userName;
    private String city;
    private String street;
    private String state;
    private String country;
    private String zipCode;
    private String cardNumber;
    private String cardHolderName;
    private LocalDate cardExpiration;
    private String cardSecurityNumber;
    private int cardTypeId;

    public CreateOrderCommand() {
        orderItems = new ArrayList<OrderItemDTO>();
    }

    public CreateOrderCommand(List<BasketItem> basketItems, String userId, String userName, String city, String street,
            String state, String country, String zipcode,
            String cardNumber, String cardHolderName, LocalDate cardExpiration,
            String cardSecurityNumber, int cardTypeId) {
        super();
        orderItems = basketItems.stream()
                .map(item -> new OrderItemDTO(item.productId(), item.productName(), item.unitPrice(), 0,
                        item.quantity(), item.pictureUrl()))
                .toList();

        this.userId = userId;
        this.userName = userName;
        this.city = city;
        this.street = street;
        this.state = state;
        this.country = country;
        this.zipCode = zipcode;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cardExpiration = cardExpiration;
        this.cardSecurityNumber = cardSecurityNumber;
        this.cardTypeId = cardTypeId;
    }

}
