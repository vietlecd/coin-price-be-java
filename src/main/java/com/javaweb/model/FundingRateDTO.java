package com.javaweb.model;

public class FundingRateDTO {
    private String symbol;
    private String fundingRate;
    private String countdownFormatted;
    private String intervalFormatted;
    private String time;

    // Constructor private để chỉ có thể tạo đối tượng thông qua Builder
    private FundingRateDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.fundingRate = builder.fundingRate;
        this.countdownFormatted = builder.countdownFormatted;
        this.intervalFormatted = builder.intervalFormatted;
        this.time = builder.time;
    }

    // Builder class
    public static class Builder {
        private String symbol;
        private String fundingRate;
        private String countdownFormatted;
        private String intervalFormatted;
        private String time;

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder fundingRate(String fundingRate) {
            this.fundingRate = fundingRate;
            return this;
        }

        public Builder countdownFormatted(String countdownFormatted) {
            this.countdownFormatted = countdownFormatted;
            return this;
        }

        public Builder intervalFormatted(String intervalFormatted) {
            this.intervalFormatted = intervalFormatted;
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

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getFundingRate() {
        return fundingRate;
    }

    public void setFundingRate(String fundingRate) {
        this.fundingRate = fundingRate;
    }

    public String getCountdownFormatted() {
        return countdownFormatted;
    }

    public void setCountdownFormatted(String countdownFormatted) {
        this.countdownFormatted = countdownFormatted;
    }

    public String getIntervalFormatted() {
        return intervalFormatted;
    }

    public void setIntervalFormatted(String intervalFormatted) {
        this.intervalFormatted = intervalFormatted;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
