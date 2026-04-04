package com.eshoponcontainers.orderingnotification.sse;

public class NotificationMessage {
    private String notificationType;
    private Integer orderId;
    private String status;
    private Integer productId;
    private Double oldPrice;
    private Double newPrice;

    public NotificationMessage(int orderId, String status) {
        this.notificationType = "order-status";
        this.orderId = orderId;
        this.status = status;
    }

    private NotificationMessage(String notificationType, Integer productId, Double oldPrice, Double newPrice) {
        this.notificationType = notificationType;
        this.productId = productId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }

    public static NotificationMessage basketPriceChange(int productId, double oldPrice, double newPrice) {
        return new NotificationMessage("basket-price-change", productId, oldPrice, newPrice);
    }

    public String getNotificationType() { return notificationType; }
    public Integer getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public Integer getProductId() { return productId; }
    public Double getOldPrice() { return oldPrice; }
    public Double getNewPrice() { return newPrice; }
}
