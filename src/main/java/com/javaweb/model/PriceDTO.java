package com.javaweb.model;

public class PriceDTO {
    private String symbol;
    private String price;

    public PriceDTO() {}

    // Constructor có 2 tham số
    public PriceDTO(String symbol, String price) {
        this.symbol = symbol;
        this.price = price;
    }

    // Getters và Setters
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
}
