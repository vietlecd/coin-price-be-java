package com.javaweb.dto.trigger;


public class ListingDTO {
    private String eventType;
    private String symbol;

    public ListingDTO(String eventType, String symbol) {
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
}
