package com.javaweb.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = FundingIntervalDTO.Builder.class)
public class FundingIntervalDTO {
    private final String symbol;
    private final String adjustedFundingRateCap;
    private final String adjustedFundingRateFloor;
    private final Long fundingIntervalHours;

    private FundingIntervalDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.adjustedFundingRateCap = builder.adjustedFundingRateCap;
        this.adjustedFundingRateFloor = builder.adjustedFundingRateFloor;
        this.fundingIntervalHours = builder.fundingIntervalHours;
    }

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

}
