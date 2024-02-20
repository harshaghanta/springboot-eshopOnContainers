package com.eshoponcontainers.seedWork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import an.awesome.pipelinr.Notification;



public abstract class Entity {

    private Integer requestedHashCode;
    private Integer id;

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    private List<Notification> domainEvents;

    public Collection<Notification> getDomainEvents() {
        if (domainEvents == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(domainEvents);
    }

    public void addDomainEvent(Notification eventItem) {
        if (domainEvents == null)
            domainEvents = new ArrayList<>();

        domainEvents.add(eventItem);
    }

    public void removeDomainEvent(Notification eventItem) {
        if (domainEvents != null)
            domainEvents.remove(eventItem);
    }

    public void clearDomainEvents() {
        if (domainEvents != null)
            domainEvents.clear();
    }

    public boolean isTransient() {
        return this.id == null;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof Entity))
            return false;

        if (this == obj)
            return true;

        if (obj.getClass() != this.getClass())
            return false;

        Entity item = (Entity) obj;

        if (item.isTransient() || this.isTransient())
            return false;
        else
            return this.id == item.id;
    }

    @Override
    public int hashCode() {
        if (!isTransient()) {
            if (requestedHashCode == null)
                requestedHashCode = this.getId().hashCode() ^ 31;
            return requestedHashCode.intValue();
        } else
            return super.hashCode();
    }
}
