package com.javaweb.dto.trigger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = ListingDTO.Builder.class)
public class ListingDTO {

    private final String symbol; // Trường symbol
    private final String notificationMethod;

    private ListingDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.notificationMethod = builder.notificationMethod;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String symbol;
        private String notificationMethod;

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder setNotificationMethod(String notificationMethod) {
            this.notificationMethod = notificationMethod;
            return this;
        }

        public ListingDTO build() {
            return new ListingDTO(this);
        }
    }
}
