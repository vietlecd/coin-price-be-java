package com.javaweb.model.trigger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "delistings")
public class DelistingEntity {

    @Id
    private String title;
    private String notificationMethod;
    private boolean isActive = false;

    public DelistingEntity() {}

    public DelistingEntity(String title, String notificationMethod, boolean isActive) {
        this.title = title;
        this.notificationMethod = notificationMethod;
        this.isActive = isActive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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