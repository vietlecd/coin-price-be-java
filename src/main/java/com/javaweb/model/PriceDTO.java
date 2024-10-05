package com.javaweb.model;

public class PriceDTO {
    private String symbol;


    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String closePrice;
    private String volume;
    private String numberOfTrades;
    private String isKlineClosed;
    private String baseAssetVolume;
    private String takerBuyVolume;
    private String takerBuyBaseVolume;
    private String eventTime;
    private String klineStartTime;
    private String klineCloseTime;

    public PriceDTO() {}


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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

    public String getNumberOfTrades() {
        return numberOfTrades;
    }

    public void setNumberOfTrades(String numberOfTrades) {
        this.numberOfTrades = numberOfTrades;
    }

    public String getIsKlineClosed() {
        return isKlineClosed;
    }

    public void setIsKlineClosed(String isKlineClosed) {
        this.isKlineClosed = isKlineClosed;
    }

    public String getBaseAssetVolume() {
        return baseAssetVolume;
    }

    public void setBaseAssetVolume(String baseAssetVolume) {
        this.baseAssetVolume = baseAssetVolume;
    }

    public String getTakerBuyVolume() {
        return takerBuyVolume;
    }

    public void setTakerBuyVolume(String takerBuyVolume) {
        this.takerBuyVolume = takerBuyVolume;
    }

    public String getTakerBuyBaseVolume() {
        return takerBuyBaseVolume;
    }

    public void setTakerBuyBaseVolume(String takerBuyBaseVolume) {
        this.takerBuyBaseVolume = takerBuyBaseVolume;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getKlineStartTime() {
        return klineStartTime;
    }

    public void setKlineStartTime(String klineStartTime) {
        this.klineStartTime = klineStartTime;
    }

    public String getKlineCloseTime() {
        return klineCloseTime;
    }

    public void setKlineCloseTime(String klineCloseTime) {
        this.klineCloseTime = klineCloseTime;
    }

    public PriceDTO(String symbol, String openPrice, String closePrice, String highPrice, String lowPrice, String volume, String numberOfTrades,
                    String isKlineClosed,
                    String baseAssetVolume,
                    String takerBuyVolume,
                    String takerBuyBaseVolume,
                    String eventTime,
                    String klineStartTime,
                    String klineCloseTime) {
        this.symbol = symbol;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.volume = volume;
        this.numberOfTrades = numberOfTrades;
        this.isKlineClosed = isKlineClosed;
        this.baseAssetVolume = baseAssetVolume;
        this.takerBuyVolume = takerBuyVolume;
        this.takerBuyBaseVolume = takerBuyBaseVolume;
        this.eventTime = eventTime;
        this.klineStartTime=klineStartTime;
        this.klineCloseTime = klineCloseTime;
    }

}
