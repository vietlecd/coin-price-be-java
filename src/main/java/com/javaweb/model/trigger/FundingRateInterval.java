package com.javaweb.model.trigger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "fundingRateIntervals")
public class FundingRateInterval {

    @Id
    private String id; 
    private String symbol;
    private long fundingTime;
    private long nextFundingTime;

    public long getIntervalTime() {
        return (nextFundingTime - fundingTime) / (1000 * 60 * 60); // Calculate intervalTime in hours
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getFundingTime() {
        return fundingTime;
    }

    public void setFundingTime(long fundingTime) {
        this.fundingTime = fundingTime;
    }

    public long getNextFundingTime() {
        return nextFundingTime;
    }

    public void setNextFundingTime(long nextFundingTime) {
        this.nextFundingTime = nextFundingTime;
    }

    private LocalDateTime timestamp;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
