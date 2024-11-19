package com.javaweb.dto.trigger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = DelistingDTO.Builder.class)
public class DelistingDTO {

    private final String title;
    private final String notificationMethod;

    private DelistingDTO(Builder builder) {
        this.title = builder.title;
        this.notificationMethod = builder.notificationMethod;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String title; // Đổi từ symbol sang title
        private String notificationMethod;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setNotificationMethod(String notificationMethod) {
            this.notificationMethod = notificationMethod;
            return this;
        }

        public DelistingDTO build() {
            return new DelistingDTO(this);
        }
    }
}
