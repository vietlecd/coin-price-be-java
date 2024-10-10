package com.javaweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = TriggerConditionDTO.Builder.class)
public class TriggerConditionDTO {
    private final String symbol;
    private final double thresholdValue;
    private final String comparisonOperator;
    private final String action;

    private TriggerConditionDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.thresholdValue = builder.thresholdValue;
        this.comparisonOperator = builder.comparisonOperator;
        this.action = builder.action;
    }

    public static class Builder {
        @JsonProperty("symbol")
        private String symbol;

        @JsonProperty("thresholdValue")
        private double thresholdValue;

        @JsonProperty("comparisonOperator")
        private String comparisonOperator;

        @JsonProperty("action")
        private String action;

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

        public TriggerConditionDTO build() {
            return new TriggerConditionDTO(this);
        }
    }
}
