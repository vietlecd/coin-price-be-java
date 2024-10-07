package com.javaweb.model;

public class PriceMarketDTO {
    private String symbol;
    private String marketCap;
    private String tradingVolume;
    public PriceMarketDTO() {}
    public PriceMarketDTO(String symbol, String marketCap, String tradingVolume) {
        this.symbol = symbol;
        this.tradingVolume=tradingVolume;
        this.marketCap=marketCap;
    }
}
