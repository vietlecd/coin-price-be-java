package com.javaweb.dto.snooze;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "snooze_conditions")
public class SnoozeCondition {

    @Id
    private String id;
    private String triggerId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
