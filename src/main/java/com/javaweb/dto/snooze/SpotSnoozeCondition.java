package com.javaweb.dto.snooze;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "spot_snooze_conditions")
public class SnoozeCondition {

    @Id
    private String symbol;
    private String triggerId;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    private String conditionType; // "One-time", "Once-in-duration", "Repeat"
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String specificTime; // sửa lại từ SpecificTime thành specificTime


    public String getSpecificTime() {
        return specificTime;
    }

    public void setSpecificTime(String specificTime) {
        this.specificTime = specificTime;
    }

    // Constructors
    public SnoozeCondition() {}

    public SnoozeCondition(String triggerId, String conditionType, LocalDateTime startTime, LocalDateTime endTime, String specificTime) {
        this.triggerId = triggerId;
        this.conditionType = conditionType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.specificTime =specificTime;
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
