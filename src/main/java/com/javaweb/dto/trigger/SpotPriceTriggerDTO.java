//package com.javaweb.dto.trigger;
//
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
//import lombok.Getter;
//
//@Getter
//@JsonDeserialize(builder = SpotPriceTriggerDTO.Builder.class)
//public class SpotPriceTriggerDTO {
//
//    private final String symbol;
//    private final String condition;
//    private final String notification_method;
//    private final double price;
//
//    private SpotPriceTriggerDTO(Builder builder) {
//        this.symbol = builder.symbol;
//        this.condition = builder.condition;
//        this.notification_method = builder.notification_method;
//        this.price = builder.price;
//    }
//
//    @JsonPOJOBuilder(withPrefix = "set")
//    public static class Builder {
//        private String symbol;
//        private String condition;
//        private String notification_method;
//        private double price;
//
//        public Builder setSymbol(String symbol) {
//            this.symbol = symbol;
//            return this;
//        }
//
//        public Builder setCondition(String condition) {
//            this.condition = condition;
//            return this;
//        }
//
//        public Builder setNotification_method(String notification_method) {
//            this.notification_method = notification_method;
//            return this;
//        }
//
//        public Builder setPrice(double price) {
//            this.price = price;
//            return this;
//        }
//
//        public SpotPriceTriggerDTO build() {
//            return new SpotPriceTriggerDTO(this);
//        }
//    }
//}
