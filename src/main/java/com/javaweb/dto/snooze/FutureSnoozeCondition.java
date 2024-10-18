package com.javaweb.dto.snooze;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "future_snooze_conditions")
public class FutureSnoozeCondition {

    @Id
    private String symbol;
    private String triggerId;

    private String usernameId;
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    private String conditionType; // "One-time", "Once-in-duration", "Repeat"

    public String getUsernameId() {
        return usernameId;
    }

    public void setUsernameId(String usernameId) {
        this.usernameId = usernameId;
    }

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
    public FutureSnoozeCondition() {}

    public FutureSnoozeCondition(String usernameId,String symbol, String conditionType, LocalDateTime startTime, LocalDateTime endTime, String specificTime) {
        this.usernameId = usernameId;
        this.symbol = symbol;
        this.conditionType = conditionType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.specificTime = specificTime;
    }

    // Getters and Setters

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public String getSnoozeType() {
        return conditionType;
    }

    public void setSnoozeType(String conditionType) {
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
