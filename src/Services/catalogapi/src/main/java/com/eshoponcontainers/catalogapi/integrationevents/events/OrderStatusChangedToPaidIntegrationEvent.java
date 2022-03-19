package com.eshoponcontainers.catalogapi.integrationevents.events;

import java.util.List;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

public class OrderStatusChangedToPaidIntegrationEvent extends IntegrationEvent {

    private Integer orderId;
    private List<OrderStockItem> items;

    public OrderStatusChangedToPaidIntegrationEvent(Integer orderId, List<OrderStockItem> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public class OrderStockItem {

        private Integer orderId;
        private Integer productId;

        public OrderStockItem(Integer orderId, Integer productId) {
            this.orderId = orderId;
            this.productId = productId;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }
    }
    
}
