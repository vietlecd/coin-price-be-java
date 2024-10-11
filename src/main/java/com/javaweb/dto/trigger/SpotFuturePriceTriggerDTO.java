package com.javaweb.dto.trigger;

import lombok.Getter;

@Getter
public class SpotFuturePriceTriggerDTO {
    private final String symbol;
    private final double thresholdValue;
    private final String comparisonOperator;
    private final String action;
    private final double spotPriceThreshold;
    private final double futurePriceThreshold;

    private SpotFuturePriceTriggerDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.thresholdValue = builder.thresholdValue;
        this.comparisonOperator = builder.comparisonOperator;
        this.action = builder.action;
        this.spotPriceThreshold = builder.spotPriceThreshold;
        this.futurePriceThreshold = builder.futurePriceThreshold;
    }

    public static class Builder {
        private String symbol;
        private double thresholdValue;
        private String comparisonOperator;
        private String action;
        private double spotPriceThreshold;
        private double futurePriceThreshold;

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder setThresholdValue(double thresholdValue) {
            this.thresholdValue = thresholdValue;
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

        public Builder setSpotPriceThreshold(double spotPriceThreshold) {
            this.spotPriceThreshold = spotPriceThreshold;
            return this;
        }

        public Builder setFuturePriceThreshold(double futurePriceThreshold) {
            this.futurePriceThreshold = futurePriceThreshold;
            return this;
        }

        public SpotFuturePriceTriggerDTO build() {
            return new SpotFuturePriceTriggerDTO(this);
        }
    }
}
