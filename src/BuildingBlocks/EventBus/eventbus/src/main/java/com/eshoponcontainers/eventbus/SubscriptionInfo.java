package com.eshoponcontainers.eventbus;

public class SubscriptionInfo {
    
    private Boolean isDynamic = false;
    private Class eventHandler = null;

    private SubscriptionInfo(Boolean isDynamic, Class eventHandler) {
        this.isDynamic = isDynamic;
        this.eventHandler = eventHandler;
    }

    public static  SubscriptionInfo typed(Class eventHandler) {
        return new SubscriptionInfo(false, eventHandler);
    }

    public Class getHandler() {
        return this.eventHandler;
    }
}
