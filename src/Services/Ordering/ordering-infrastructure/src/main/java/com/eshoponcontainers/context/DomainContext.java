package com.eshoponcontainers.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import an.awesome.pipelinr.Notification;

public class DomainContext {

    private static ThreadLocal<Set<Notification>> threadLocal = new ThreadLocal<>();

    public static void addDomainEvents(Collection<Notification> notifications) {
        Set<Notification> notificationList = threadLocal.get();
        if(notificationList == null)
            notificationList = new HashSet<>();
        notificationList.addAll(notifications);
        threadLocal.set(notificationList);
    }

    public static Set<Notification> getDomainEvents() {
        return threadLocal.get();
    }

    public static void clearContext() {
        threadLocal.remove();
    }
}
