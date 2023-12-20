package com.eshoponcontainers.aggregatesModel.orderAggregate;

import com.eshoponcontainers.exceptions.OrderingDomainException;
import com.eshoponcontainers.seedWork.Entity;

import lombok.Getter;

@Getter
public class OrderItem extends Entity {
    private int productId;
    private String productName;
    private String pictureUrl;
    private double unitPrice;
    private double discount;
    private int units;

    protected OrderItem() {

    }

    public OrderItem(int productId, String productName, double unitPrice, double discount, String pictureUrl, int units) throws OrderingDomainException {
        if(units <= 0)
            throw new OrderingDomainException("Invalid number of units");

        if((unitPrice * units) < discount)
            throw new OrderingDomainException("The total of order item is lower than applied discount");

        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.units = units;
    }

    public void setNewDiscount(double discount) throws OrderingDomainException {
        if(discount < 0)
            throw new OrderingDomainException("Discount is invalid");

        this.discount = discount;
    }

    public void addUnits(int units) throws OrderingDomainException {
        if(units < 0)
            throw new OrderingDomainException("Invalid number of units");

        this.units += units;
    }
 
}