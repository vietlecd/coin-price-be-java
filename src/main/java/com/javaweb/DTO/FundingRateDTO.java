package com.javaweb.DTO;

public class FundingRateDTO {
    private String symbol;
    private String fundingRate;
    private String fundingCountdown;
    private String time;

    private FundingRateDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.fundingRate = builder.fundingRate;
        this.fundingCountdown = builder.fundingCountdown;
        this.time = builder.time;
    }

    // Builder class
    public static class Builder {
        private String symbol;
        private String fundingRate;
        private String fundingCountdown;
        private String time;

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

        public Builder time(String time) {
            this.time = time;
            return this;
        }

        public FundingRateDTO build() {
            return new FundingRateDTO(this);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public String getFundingRate() {
        return fundingRate;
    }

    public String getFundingCountdown() {
        return fundingCountdown;
    }

    public String getTime() {
        return time;
    }

}
