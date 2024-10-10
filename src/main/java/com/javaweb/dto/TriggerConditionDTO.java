package com.javaweb.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = TriggerConditionDTO.Builder.class)
public class TriggerConditionDTO {
    private final String spotPrice;
    private final String futurePrice;
    private final String fundingRate;
    private final boolean fundingRateInterval;
    private final String symbol;

    private TriggerConditionDTO(Builder builder) {
        this.spotPrice = builder.spotPrice;
        this.futurePrice = builder.futurePrice;
        this.fundingRate = builder.fundingRate;
        this.fundingRateInterval = builder.fundingRateInterval;
        this.symbol = builder.symbol;
    }

    // Builder Class
    public static class Builder {
        private String spotPrice;
        private String futurePrice;
        private String fundingRate;
        private boolean fundingRateInterval;

        private String symbol;


        public Builder spotPrice(String spotPrice) {
            this.spotPrice = spotPrice;
            return this;
        }

        public Builder futurePrice(String futurePrice) {
            this.futurePrice = futurePrice;
            return this;
        }

        public Builder fundingRate(String fundingRate) {
            this.fundingRate = fundingRate;
            return this;
        }

        public Builder fundingRateInterval(boolean fundingRateInterval) {
            this.fundingRateInterval = fundingRateInterval;
            return this;
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public TriggerConditionDTO build() {
            return new TriggerConditionDTO(this);
        }
    }
}
