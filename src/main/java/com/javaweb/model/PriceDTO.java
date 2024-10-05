package com.javaweb.model;

public class PriceDTO {
    private String symbol;
    private String price;
    private String openTime;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String closePrice;
    private String volume;
    private String closeTime;
    private String baseAssetVolume;
    private String numberOfTrades;
    private String takerBuyVolume;
    private String takerBuyBaseAssetVolume;
    private Long ignore;

    public PriceDTO() {}

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = closePrice;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getBaseAssetVolume() {
        return baseAssetVolume;
    }

    public void setBaseAssetVolume(String baseAssetVolume) {
        this.baseAssetVolume = baseAssetVolume;
    }

    public String getNumberOfTrades() {
        return numberOfTrades;
    }

    public void setNumberOfTrades(String numberOfTrades) {
        this.numberOfTrades = numberOfTrades;
    }

    public String getTakerBuyVolume() {
        return takerBuyVolume;
    }

    public void setTakerBuyVolume(String takerBuyVolume) {
        this.takerBuyVolume = takerBuyVolume;
    }

    public String getTakerBuyBaseAssetVolume() {
        return takerBuyBaseAssetVolume;
    }

    public void setTakerBuyBaseAssetVolume(String takerBuyBaseAssetVolume) {
        this.takerBuyBaseAssetVolume = takerBuyBaseAssetVolume;
    }

    public Long getIgnore() {
        return ignore;
    }

    public void setIgnore(Long ignore) {
        this.ignore = ignore;
    }

    public PriceDTO(String symbol, String openPrice, String closePrice, String highPrice, String lowPrice) {
        this.symbol = symbol;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
    }

}
