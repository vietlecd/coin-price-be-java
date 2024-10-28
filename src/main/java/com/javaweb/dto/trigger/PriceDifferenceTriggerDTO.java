package com.javaweb.dto.trigger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = PriceDifferenceTriggerDTO.Builder.class)
public class PriceDifferenceTriggerDTO {
    private final String symbol;
    private final String condition;
    private final String notification_method;
    private final double spotPrice;
    private final double futurePrice;
    private final double priceDifference;

    private PriceDifferenceTriggerDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.condition = builder.condition;
        this.notification_method = builder.notification_method;
        this.spotPrice = builder.spotPrice;
        this.futurePrice = builder.futurePrice;
        this.priceDifference = builder.priceDifference;
    }


    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String symbol;
        private String condition;
        private String notification_method;
        private double spotPrice;
        private double futurePrice;
        private double priceDifference;

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }


        public Builder setCondition(String condition) {
            this.condition = condition;
            return this;
        }

        public Builder setNotification_method(String notification_method) {
            this.notification_method = notification_method;
            return this;
        }

        public Builder setSpotPrice(double spotPrice) {
            this.spotPrice = spotPrice;
            return this;
        }

        public Builder setFuturePrice(double futurePrice) {
            this.futurePrice = futurePrice;
            return this;
        }

        public Builder setPriceDifference(double priceDifference) {
            this.priceDifference = priceDifference;
            return this;
        }

        public PriceDifferenceTriggerDTO build() {
            return new PriceDifferenceTriggerDTO(this);
        }
    }
}
