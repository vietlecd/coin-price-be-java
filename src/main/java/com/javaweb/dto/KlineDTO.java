package com.javaweb.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = KlineDTO.Builder.class)
public class KlineDTO {
    private String symbol;
    private String eventTime;
    private String klineStartTime;
    private String klineCloseTime;
    private String openPrice;
    private String closePrice;
    private String highPrice;
    private String lowPrice;
    private Long numberOfTrades;
    private String baseAssetVolume;
    private String takerBuyVolume;
    private String takerBuyBaseVolume;
    private String volume;

    private KlineDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.eventTime = builder.eventTime;
        this.klineStartTime = builder.klineStartTime;
        this.klineCloseTime = builder.klineCloseTime;
        this.openPrice = builder.openPrice;
        this.closePrice = builder.closePrice;
        this.highPrice = builder.highPrice;
        this.lowPrice = builder.lowPrice;
        this.numberOfTrades = builder.numberOfTrades;
        this.baseAssetVolume = builder.baseAssetVolume;
        this.takerBuyVolume = builder.takerBuyVolume;
        this.takerBuyBaseVolume = builder.takerBuyBaseVolume;
        this.volume = builder.volume;
    }


    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String symbol;
        private String eventTime;
        private String klineStartTime;
        private String klineCloseTime;
        private String openPrice;
        private String closePrice;
        private String highPrice;
        private String lowPrice;
        private Long numberOfTrades;
        private String baseAssetVolume;
        private String takerBuyVolume;
        private String takerBuyBaseVolume;
        private String volume;

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder eventTime(String eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public Builder klineStartTime(String klineStartTime) {
            this.klineStartTime = klineStartTime;
            return this;
        }

        public Builder klineCloseTime(String klineCloseTime) {
            this.klineCloseTime = klineCloseTime;
            return this;
        }

        public Builder openPrice(String openPrice) {
            this.openPrice = openPrice;
            return this;
        }

        public Builder closePrice(String closePrice) {
            this.closePrice = closePrice;
            return this;
        }

        public Builder highPrice(String highPrice) {
            this.highPrice = highPrice;
            return this;
        }

        public Builder lowPrice(String lowPrice) {
            this.lowPrice = lowPrice;
            return this;
        }

        public Builder numberOfTrades(Long numberOfTrades) {
            this.numberOfTrades = numberOfTrades;
            return this;
        }

        public Builder baseAssetVolume(String baseAssetVolume) {
            this.baseAssetVolume = baseAssetVolume;
            return this;
        }

        public Builder takerBuyVolume(String takerBuyVolume) {
            this.takerBuyVolume = takerBuyVolume;
            return this;
        }

        public Builder takerBuyBaseVolume(String takerBuyBaseVolume) {
            this.takerBuyBaseVolume = takerBuyBaseVolume;
            return this;
        }

        public Builder volume(String volume) {
            this.volume = volume;
            return this;
        }

        public KlineDTO build() {
            return new KlineDTO(this);
        }
    }
}
