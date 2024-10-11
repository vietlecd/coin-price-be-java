package com.javaweb.dto.trigger;

import lombok.Getter;

@Getter
public class PriceDifferenceTriggerDTO {
    private final String symbol;
    private final String comparisonOperator;
    private final String action;
    private final double priceDifferenceThreshold;

    private PriceDifferenceTriggerDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.comparisonOperator = builder.comparisonOperator;
        this.action = builder.action;
        this.priceDifferenceThreshold = builder.priceDifferenceThreshold;
    }

    public static class Builder {
        private String symbol;
        private String comparisonOperator;
        private String action;
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

        public Builder setPriceDifferenceThreshold(double priceDifferenceThreshold) {
            this.priceDifferenceThreshold = priceDifferenceThreshold;
            return this;
        }

        public PriceDifferenceTriggerDTO build() {
            return new PriceDifferenceTriggerDTO(this);
        }
    }
}
