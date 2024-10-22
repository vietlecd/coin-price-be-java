package com.javaweb.model.mongo_entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Otp {
    private String otpCode;
    private LocalDateTime expiryDate;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    };
}
