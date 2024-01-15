package com.eshoponcontainers.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import an.awesome.pipelinr.Notification;

public class DomainContext {

    private static ThreadLocal<List<Notification>> threadLocal = new ThreadLocal<>();

    public static void addDomainEvents(Collection<Notification> notifications) {
        List<Notification> notificationList = threadLocal.get();
        if(notificationList == null)
            notificationList = new ArrayList<>();
        notificationList.addAll(notifications);
        threadLocal.set(notificationList);
    }

    public static List<Notification> getDomainEvents() {
        return threadLocal.get();
    }

    public static void clearContext() {
        threadLocal.remove();
    }
}
