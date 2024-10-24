package com.javaweb.dto.snooze;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Document(collection = "indicator_snooze_conditions")
@Component
public class IndicatorSnoozeCondition {

    @Id
    private String id; // Sử dụng id để quản lý khóa chính trong MongoDB

    private String username; // Thay thế symbol bằng usernameId làm khóa chính

    private String symbol;

    private String conditionType; // "One-time", "Once-in-duration", "Repeat"
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String specificTime; // sửa lại từ SpecificTime thành specificTime

    // Thêm các thuộc tính cho Repeat Count
    private int repeatCount; // Số lần snooze đã kích hoạt
    private int maxRepeatCount; // Số lần tối đa có thể kích hoạt snooze

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSpecificTime() {
        return specificTime;
    }

    public void setSpecificTime(String specificTime) {
        this.specificTime = specificTime;
    }

    // Constructors
    public IndicatorSnoozeCondition() {}

    public IndicatorSnoozeCondition(String symbol, String conditionType, LocalDateTime startTime, LocalDateTime endTime, String specificTime) {
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

    // Thêm các phương thức liên quan đến Repeat Count
    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getMaxRepeatCount() {
        return maxRepeatCount;
    }

    public void setMaxRepeatCount(int maxRepeatCount) {
        this.maxRepeatCount = maxRepeatCount;
    }
}
