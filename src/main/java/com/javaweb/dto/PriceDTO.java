package com.javaweb.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = PriceDTO.Builder.class)
public class PriceDTO {
    private final String price;
    private final String eventTime;
    private final String symbol;

    private PriceDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.price = builder.price;
        this.eventTime = builder.eventTime;
    }

    public static class Builder {
        private String symbol;
        private String price;
        private String eventTime;


        public Builder price(String price) {
            this.price = price;
            return this;
        }

        public Builder eventTime(String eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public PriceDTO build() {
            return new PriceDTO(this);
        }
    }
}
