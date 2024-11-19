package com.javaweb.model.trigger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "listings")
public class ListingEntity {

    @Id
    private String symbol;
    private String notificationMethod;
    private boolean isActive = false;


    public ListingEntity() {}


    public ListingEntity(String symbol, String notificationMethod, boolean isActive) {
        this.symbol = symbol;
        this.notificationMethod = notificationMethod;
        this.isActive = isActive;
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getNotificationMethod() {
        return notificationMethod;
    }

    public void setNotificationMethod(String notificationMethod) {
        this.notificationMethod = notificationMethod;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
