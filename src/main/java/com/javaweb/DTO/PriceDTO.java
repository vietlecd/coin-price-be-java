package com.javaweb.DTO;

public class PriceDTO {
    private String price;
    private String eventTime;
    private String symbol;

    // Private constructor, chỉ có thể tạo đối tượng qua Builder
    private PriceDTO(Builder builder) {
        this.price = builder.price;
        this.eventTime = builder.eventTime;
        this.symbol = builder.symbol;
    }

    // Static inner Builder class
    public static class Builder {
        private String price;
        private String eventTime;
        private String symbol;

        public Builder price(String price) {
            this.price = price;
            return this;
        }

        public Builder eventTime(String eventTime) {
            this.eventTime = eventTime;
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

    public String getEventTime() {
        return eventTime;
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
