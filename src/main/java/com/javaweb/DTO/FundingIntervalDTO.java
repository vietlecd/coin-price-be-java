package com.javaweb.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = FundingIntervalDTO.Builder.class)
public class FundingIntervalDTO {
    private String symbol;
    private String adjustedFundingRateCap;
    private String adjustedFundingRateFloor;
    private Long fundingIntervalHours;

    private FundingIntervalDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.adjustedFundingRateCap = builder.adjustedFundingRateCap;
        this.adjustedFundingRateFloor = builder.adjustedFundingRateFloor;
        this.fundingIntervalHours = builder.fundingIntervalHours;
    }

    // Builder class
    public static class Builder {
        private String symbol;
        private String adjustedFundingRateCap;
        private String adjustedFundingRateFloor;
        private Long fundingIntervalHours;

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder adjustedFundingRateCap(String adjustedFundingRateCap) {
            this.adjustedFundingRateCap = adjustedFundingRateCap;
            return this;
        }

        public Builder adjustedFundingRateFloor(String adjustedFundingRateFloor) {
            this.adjustedFundingRateFloor = adjustedFundingRateFloor;
            return this;
        }

        public Builder fundingIntervalHours(Long fundingIntervalHours) {
            this.fundingIntervalHours = fundingIntervalHours;
            return this;
        }

        public FundingIntervalDTO build() {
            return new FundingIntervalDTO(this);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAdjustedFundingRateCap() {
        return adjustedFundingRateCap;
    }

    public String getAdjustedFundingRateFloor() {
        return adjustedFundingRateFloor;
    }

    public Long getFundingIntervalHours() {
        return fundingIntervalHours;
    }
}
