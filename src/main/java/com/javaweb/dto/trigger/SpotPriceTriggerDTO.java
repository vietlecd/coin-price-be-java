package com.javaweb.dto.trigger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = SpotPriceTriggerDTO.Builder.class)
public class SpotPriceTriggerDTO {
    private final String symbol;
    private final String comparisonOperator;
    private final String action;
    private final double spotPriceThreshold;

    private SpotPriceTriggerDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.comparisonOperator = builder.comparisonOperator;
        this.action = builder.action;
        this.spotPriceThreshold = builder.spotPriceThreshold;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String symbol;
        private String comparisonOperator;
        private String action;
        private double spotPriceThreshold;

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

        public Builder setSpotPriceThreshold(double spotPriceThreshold) {
            this.spotPriceThreshold = spotPriceThreshold;
            return this;
        }

        public SpotPriceTriggerDTO build() {
            return new SpotPriceTriggerDTO(this);
        }
    }
}
