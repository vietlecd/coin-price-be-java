package com.javaweb.model;

public class PriceDTO {
    private String price;
    private String time;
    private String symbol;

    // Private constructor, chỉ có thể tạo đối tượng qua Builder
    private PriceDTO(Builder builder) {
        this.price = builder.price;
        this.time = builder.time;
        this.symbol = builder.symbol;
    }

    // Static inner Builder class
    public static class Builder {
        private String price;
        private String time;
        private String symbol;

        public Builder price(String price) {
            this.price = price;
            return this;
        }

        public Builder time(String time) {
            this.time = time;
            return this;
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public PriceDTO build() {
            return new PriceDTO(this);
        }
    }

    // Getters

    public String getTime() {
        return time;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }
}
