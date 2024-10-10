package com.javaweb.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = FundingRateDTO.Builder.class)
public class FundingRateDTO {
    private final String symbol;
    private final String fundingRate;
    private final String fundingCountdown;
    private final String eventTime;

    private FundingRateDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.fundingRate = builder.fundingRate;
        this.fundingCountdown = builder.fundingCountdown;
        this.eventTime = builder.eventTime;
    }

    // Builder class
    public static class Builder {
        private String symbol;
        private String fundingRate;
        private String fundingCountdown;
        private String eventTime;

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder fundingRate(String fundingRate) {
            this.fundingRate = fundingRate;
            return this;
        }

        public Builder fundingCountdown(String fundingCountdown) {
            this.fundingCountdown = fundingCountdown;
            return this;
        }

        public Builder eventTime(String eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public FundingRateDTO build() {
            return new FundingRateDTO(this);
        }
    }

}
