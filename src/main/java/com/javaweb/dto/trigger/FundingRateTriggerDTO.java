//package com.javaweb.dto.trigger;
//
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
//import lombok.Getter;
//
//@Getter
//@JsonDeserialize(builder = FundingRateTriggerDTO.Builder.class)
//public class FundingRateTriggerDTO {
//    private final String symbol;
//    private final String condition;
//    private final String notification_method;
//    private final double fundingRate;
//    //private final double fundingRateInterval;
//
//    private FundingRateTriggerDTO(Builder builder) {
//        this.symbol = builder.symbol;
//        this.condition = builder.condition;
//        this.notification_method = builder.notification_method;
//        this.fundingRate = builder.fundingRate;
//        //this.fundingRateInterval = builder.fundingRateInterval;
//    }
//
//    @JsonPOJOBuilder(withPrefix = "set")
//    public static class Builder {
//        private String symbol;
//        private String condition;
//        private String notification_method;
//        private double fundingRate;
//        //private double fundingRateInterval;
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
//        public Builder setFundingRate(double fundingRate) {
//            this.fundingRate = fundingRate;
//            return this;
//        }
//
////        public Builder setFundingRateInterval(double fundingRateInterval) {
////            this.fundingRateInterval = fundingRateInterval;
////            return this;
////        }
//
//        public FundingRateTriggerDTO build() {
//            return new FundingRateTriggerDTO(this);
//        }
//    }
//}
