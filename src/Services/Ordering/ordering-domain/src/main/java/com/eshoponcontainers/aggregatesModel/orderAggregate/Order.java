package com.eshoponcontainers.aggregatesModel.orderAggregate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.eshoponcontainers.events.OrderCancelledDomainEvent;
import com.eshoponcontainers.events.OrderShippedDomainEvent;
import com.eshoponcontainers.events.OrderStartedDomainEvent;
import com.eshoponcontainers.events.OrderStatusChangedToAwaitingValidationDomainEvent;
import com.eshoponcontainers.events.OrderStatusChangedToPaidDomainEvent;
import com.eshoponcontainers.events.OrderStatusChangedToStockConfirmedDomainEvent;
import com.eshoponcontainers.exceptions.OrderingDomainException;
import com.eshoponcontainers.seedWork.Entity;
import com.eshoponcontainers.seedWork.IAggregateRoot;

import lombok.Getter;

@Getter
public class Order extends Entity implements IAggregateRoot {

    private Instant orderDate;
    private Address address;
    private String description;
    private boolean isDraft;
    private Integer buyerId;
    private Integer paymentMethodId;
    private final List<OrderItem> orderItems;
    private OrderStatus orderStatus;

    public List<OrderItem> gerOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public static Order newDraft() {
        Order order = new Order();
        order.isDraft = true;
        return order;
    }

    protected Order() {
        orderItems = new ArrayList<>();
        isDraft = false;
    }

    public Order(String userId, String userName, Address address, int cardTypeId, String cardNumber,
            String cardSecurityNumber,
            String cardHolderName, LocalDate cardExpiration, Integer buyerId, Integer paymentMethodId) {
        this();
        this.buyerId = buyerId;
        this.paymentMethodId = paymentMethodId;
        this.orderDate = Instant.now();
        this.address = address;
        this.orderStatus = OrderStatus.Submitted;

        addOrderStartedDomainEvent(userId, userName, cardTypeId, cardNumber, cardSecurityNumber, cardHolderName,
                cardExpiration);
    }

    public void addOrderItem(int productId, String productName, double unitPrice, double discount, String pictureUrl)
            throws OrderingDomainException {
        addOrderItem(productId, productName, unitPrice, discount, pictureUrl, 1);
    }

    public void addOrderItem(int productId, String productName, double unitPrice, double discount, String pictureUrl,
            int units) throws OrderingDomainException {

        Optional<OrderItem> optionalItem = orderItems.stream().filter(item -> item.getProductId() == productId)
                .findFirst();
        if (optionalItem.isPresent()) {
            OrderItem orderItem = optionalItem.get();
            if (discount > orderItem.getDiscount()) {
                orderItem.setNewDiscount(discount);
            }
            orderItem.addUnits(units);
        } else {
            OrderItem orderItem = new OrderItem(productId, productName, unitPrice, discount, pictureUrl, units);
            orderItems.add(orderItem);
        }
    }

    public void setPaymentId(int id) {
        this.paymentMethodId = id;
    }

    public void setBuyerId(int id) {
        this.buyerId = id;
    }

    public void setAwaitingValidationStatus() {
        if (orderStatus == OrderStatus.Submitted) {
            addDomainEvent(new OrderStatusChangedToAwaitingValidationDomainEvent(this.getId(), orderItems));
            orderStatus = OrderStatus.AwaitingValidation;
        }
    }

    public void setStockConfirmedStatus() {
        if (orderStatus == OrderStatus.AwaitingValidation) {
            addDomainEvent(new OrderStatusChangedToStockConfirmedDomainEvent(this.getId()));

            orderStatus = OrderStatus.StockConfirmed;
            description = "All the items were confirmed with available stock.";
        }
    }

    public void SetPaidStatus() {
        if (orderStatus == OrderStatus.StockConfirmed) {
            addDomainEvent(new OrderStatusChangedToPaidDomainEvent(this.getId(), this.getOrderItems()));

            orderStatus = OrderStatus.Paid;
            description = "The payment was performed at a simulated \"American Bank checking bank account ending on XX35071\"";
        }
    }

    public void SetShippedStatus() throws OrderingDomainException {
        if (orderStatus != OrderStatus.Paid) {
            StatusChangeException(OrderStatus.Shipped);
        }

        orderStatus = OrderStatus.Shipped;
        description = "The order was shipped.";
        addDomainEvent(new OrderShippedDomainEvent(this));
    }

    public void SetCancelledStatus() throws OrderingDomainException {
        if (orderStatus == OrderStatus.Paid || orderStatus == OrderStatus.Shipped) {
            StatusChangeException(OrderStatus.Cancelled);
        }

        orderStatus = OrderStatus.Cancelled;
        description = "The order was cancelled.";
        addDomainEvent(new OrderCancelledDomainEvent(this));
    }

    public void SetCancelledStatusWhenStockIsRejected(List<Integer> orderStockRejectedItems) {
        if (orderStatus == OrderStatus.AwaitingValidation) {
            orderStatus = OrderStatus.Cancelled;

            List<String> itemsStockRejectedProductNames = this.orderItems.stream()
                    .filter(item -> orderStockRejectedItems.contains(item.getProductId()))
                    .map(item -> item.getProductName()).collect(Collectors.toList());

            String itemsStockRejectedDescription = String.join(", ", itemsStockRejectedProductNames);
            description = "The product items don't have stock: " + itemsStockRejectedDescription;
        }
    }

    private void StatusChangeException(OrderStatus orderStatusToChange) throws OrderingDomainException {
        throw new OrderingDomainException("Is not possible to change the order status from " + orderStatus.name()
                + " to " + orderStatusToChange.name() + ".");
    }

    private void addOrderStartedDomainEvent(String userId, String userName, int cardTypeId, String cardNumber,
            String cardSecurityNumber,
            String cardHolderName, LocalDate cardExpiration) {
        OrderStartedDomainEvent orderStartedDomainEvent = new OrderStartedDomainEvent(this, userId, userName,
                cardTypeId, cardNumber, cardSecurityNumber, cardHolderName, cardExpiration);
        this.addDomainEvent(orderStartedDomainEvent);

    }

}
