package com.javaweb.model.trigger;


public class ListingTrigger {
    private String eventType;
    private String symbol;

    public ListingTrigger(String eventType, String symbol) {
        this.eventType = eventType;
        this.symbol = symbol;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "BinanceToken{" +
                "eventType='" + eventType + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
