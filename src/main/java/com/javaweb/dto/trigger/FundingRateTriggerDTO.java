package com.javaweb.dto.trigger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = FundingRateTriggerDTO.Builder.class)
public class FundingRateTriggerDTO {
    private final String symbol;
    private final String comparisonOperator;
    private final String action;
    private final double fundingRateThreshold;
    private final double fundingRateInterval;

    private FundingRateTriggerDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.comparisonOperator = builder.comparisonOperator;
        this.action = builder.action;
        this.fundingRateThreshold = builder.fundingRateThreshold;
        this.fundingRateInterval = builder.fundingRateInterval;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String symbol;
        private String comparisonOperator;
        private String action;
        private double fundingRateThreshold;
        private double fundingRateInterval;

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

        public Builder setFundingRateThreshold(double fundingRateThreshold) {
            this.fundingRateThreshold = fundingRateThreshold;
            return this;
        }

        public Builder setFundingRateInterval(double fundingRateInterval) {
            this.fundingRateInterval = fundingRateInterval;
            return this;
        }

        public FundingRateTriggerDTO build() {
            return new FundingRateTriggerDTO(this);
        }
    }
}
