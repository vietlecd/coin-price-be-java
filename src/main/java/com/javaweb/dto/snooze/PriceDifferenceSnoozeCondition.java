package com.javaweb.dto.snooze;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "price_difference_snooze_conditions")
public class PriceDifferenceSnoozeCondition {

    @Id
    private String symbol;
    private String triggerId;
    private String usernameId;
    public String getSymbol() {
        return symbol;
    }

    public String getUsernameId() {
        return usernameId;
    }

    public void setUsernameId(String usernameId) {
        this.usernameId = usernameId;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    private String conditionType; // "One-time", "Once-in-duration", "Repeat"
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String specificTime; // Updated field name to "specificTime"

    public String getSpecificTime() {
        return specificTime;
    }

    public void setSpecificTime(String specificTime) {
        this.specificTime = specificTime;
    }

    // Constructors
    public PriceDifferenceSnoozeCondition() {}

    public PriceDifferenceSnoozeCondition(String usernameId,String symbol, String conditionType, LocalDateTime startTime, LocalDateTime endTime, String specificTime) {
        this.usernameId = usernameId;
        this.symbol = symbol;
        this.conditionType = conditionType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.specificTime = specificTime;
    }

    // Getters and Setters
    public String getSnoozeType() {
        return conditionType;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
