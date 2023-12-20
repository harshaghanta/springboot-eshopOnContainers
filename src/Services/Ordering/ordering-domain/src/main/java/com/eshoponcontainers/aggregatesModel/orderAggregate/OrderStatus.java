package com.eshoponcontainers.aggregatesModel.orderAggregate;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatus {

    Submitted(1, "submitted"),
    AwaitingValidation(2, "awaitingvalidation"),
    StockConfirmed(3, "stockconfirmed"),
    Paid(4, "paid"),
    Shipped(5, "shipped"),
    Cancelled(6, "cancelled");

    private int id;
    private String name;

    private static final Map<String, OrderStatus> by_name = new HashMap<>();
    private static final Map<Integer, OrderStatus> by_id = new HashMap<>();

    private OrderStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    static {
        for (OrderStatus status : values()) {
            by_name.put(status.name, status);
            by_id.put(status.id, status);
        }
    }
    
    public static OrderStatus fromName(String name) {
        return by_name.get(name);
    }

    public static OrderStatus from(int id) {
        return by_id.get(id);
    }
}
