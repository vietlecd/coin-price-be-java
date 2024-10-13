package com.javaweb.dto.trigger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = PriceDifferenceTriggerDTO.Builder.class)
public class PriceDifferenceTriggerDTO {
    private final String symbol;
    private final String comparisonOperator;
    private final String action;
    private final double spotPrice;
    private final double futurePrice;
    private final double priceDifferenceThreshold;

    private PriceDifferenceTriggerDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.comparisonOperator = builder.comparisonOperator;
        this.action = builder.action;
        this.spotPrice = builder.spotPrice;
        this.futurePrice = builder.futurePrice;
        this.priceDifferenceThreshold = builder.priceDifferenceThreshold;
    }


    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String symbol;
        private String comparisonOperator;
        private String action;
        private double spotPrice;
        private double futurePrice;
        private double priceDifferenceThreshold;

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }


        public Builder setComparisonOperator(String comparisonOperator) {
            this.comparisonOperator = comparisonOperator;
            return this;
        }

        public Builder setAction(String action) {
            this.action = action;
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

        public Builder setPriceDifferenceThreshold(double priceDifferenceThreshold) {
            this.priceDifferenceThreshold = priceDifferenceThreshold;
            return this;
        }

        public PriceDifferenceTriggerDTO build() {
            return new PriceDifferenceTriggerDTO(this);
        }
    }
}
