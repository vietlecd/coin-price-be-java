package com.javaweb.model.trigger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class FundingInterval {

    @Id
    private String id;
    private String symbol;
    private Long fundingIntervalHours;
    private String adjustedFundingRateCap;
    private String adjustedFundingRateFloor;

    // Constructor with all necessary fields
    public FundingInterval(String symbol, Long fundingIntervalHours, String adjustedFundingRateCap, String adjustedFundingRateFloor) {
        this.symbol = symbol;
        this.fundingIntervalHours = fundingIntervalHours;
        this.adjustedFundingRateCap = adjustedFundingRateCap;
        this.adjustedFundingRateFloor = adjustedFundingRateFloor;
    }

    // Getters and Setters for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getFundingIntervalHours() {
        return fundingIntervalHours;
    }

    public void setFundingIntervalHours(Long fundingIntervalHours) {
        this.fundingIntervalHours = fundingIntervalHours;
    }

    public String getAdjustedFundingRateCap() {
        return adjustedFundingRateCap;
    }

    public void setAdjustedFundingRateCap(String adjustedFundingRateCap) {
        this.adjustedFundingRateCap = adjustedFundingRateCap;
    }

    public String getAdjustedFundingRateFloor() {
        return adjustedFundingRateFloor;
    }

    public void setAdjustedFundingRateFloor(String adjustedFundingRateFloor) {
        this.adjustedFundingRateFloor = adjustedFundingRateFloor;
    }
}
