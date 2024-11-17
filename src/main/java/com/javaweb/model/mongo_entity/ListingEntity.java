package com.javaweb.model.mongo_entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "listings")
public class ListingEntity {

    @Id
    private String symbol;  // Mỗi listing sẽ có symbol duy nhất là id trong MongoDB
    private String notificationMethod;
    private boolean isActive = false;  // Trạng thái kích hoạt trigger

    // Constructor mặc định
    public ListingEntity() {}

    // Constructor với tham số
    public ListingEntity(String symbol, String notificationMethod, boolean isActive) {
        this.symbol = symbol;
        this.notificationMethod = notificationMethod;
        this.isActive = isActive;
    }

    // Getter và Setter
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
