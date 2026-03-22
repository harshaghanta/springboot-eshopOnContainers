package com.eshoponcontainers.orderingnotification.sse;

public class NotificationMessage {
    private int orderId;
    private String status;

    public NotificationMessage(int orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public String getStatus() { return status; }
}
