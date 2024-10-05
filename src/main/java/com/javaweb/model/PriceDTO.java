package com.javaweb.model;

public class PriceDTO {
    private String price;
    private String time;

    public PriceDTO() {}

    public PriceDTO(String time, String price) {
        this.time = time
        ;
        this.price = price;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
