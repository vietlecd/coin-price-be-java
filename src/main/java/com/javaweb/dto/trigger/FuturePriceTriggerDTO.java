package com.javaweb.dto.trigger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = FuturePriceTriggerDTO.Builder.class)
public class FuturePriceTriggerDTO {
    private final String symbol;
    private final String comparisonOperator;
    private final String action;
    private final double futurePriceThreshold;

    private FuturePriceTriggerDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.comparisonOperator = builder.comparisonOperator;
        this.action = builder.action;
        this.futurePriceThreshold = builder.futurePriceThreshold;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String symbol;
        private String comparisonOperator;
        private String action;
        private double futurePriceThreshold;

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

        public Builder setFuturePriceThreshold(double futurePriceThreshold) {
            this.futurePriceThreshold = futurePriceThreshold;
            return this;
        }

        public FuturePriceTriggerDTO build() {
            return new FuturePriceTriggerDTO(this);
        }
    }
}
