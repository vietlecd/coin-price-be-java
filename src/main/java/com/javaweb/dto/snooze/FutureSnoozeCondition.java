//package com.javaweb.dto.snooze;
//
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Document(collection = "future_snooze_conditions")
//@Component
//public class FutureSnoozeCondition {
//
//
//    private String username; // Thay thế symbol bằng usernameId làm khóa chính
//    private String symbol;
//
//    private String conditionType; // "One-time", "Once-in-duration", "Repeat"
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
//    private String specificTime; // sửa lại từ SpecificTime thành specificTime
//
//    // Constructors
//    public FutureSnoozeCondition() {}
//
//    public FutureSnoozeCondition(String symbol, String conditionType, LocalDateTime startTime, LocalDateTime endTime, String specificTime) {
//        this.symbol = symbol;
//        this.conditionType = conditionType;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.specificTime = specificTime;
//    }
//
//    // Getters and Setters
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getSymbol() {
//        return symbol;
//    }
//
//    public void setSymbol(String symbol) {
//        this.symbol = symbol;
//    }
//
//    public String getSnoozeType() {
//        return conditionType;
//    }
//
//    public void setConditionType(String conditionType) {
//        this.conditionType = conditionType;
//    }
//
//    public LocalDateTime getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(LocalDateTime startTime) {
//        this.startTime = startTime;
//    }
//
//    public LocalDateTime getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(LocalDateTime endTime) {
//        this.endTime = endTime;
//    }
//
//    public String getSpecificTime() {
//        return specificTime;
//    }
//
//    public void setSpecificTime(String specificTime) {
//        this.specificTime = specificTime;
//    }
//}