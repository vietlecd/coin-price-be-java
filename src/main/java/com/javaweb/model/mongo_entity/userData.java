package com.javaweb.model.mongo_entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.criteria.CriteriaBuilder;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "userData")
public class userData {
    private String username;
    private String name;
    private String password;
    private String email;
    private Integer vip_role;
    private List<String> ip_list;
    private Otp otp;

    public void addIp(String ip) {
        ip_list.add(ip);
    }

    public void removeIp(String ip) {
        if(ip_list.contains(ip)) ip_list.remove(ip);
    }

    public void generateOtp(int length, int validityInMinutes) {
        this.otp = new Otp();
        this.otp.setOtpCode(generateRandomOtp(length));
        this.otp.setExpiryDate(LocalDateTime.now().plusMinutes(validityInMinutes));
    }

    public boolean useOtp(String otpCode) {
        try {
            if(checkOtp(otpCode)) {
                otp = null;
                return true;
            }
            else {
                throw new Exception("Otp không tồn tại!");
            }
        } catch (Exception e) {
            return false;
        }
    }

    // private:
    private String generateRandomOtp(int length) {
        StringBuilder otpBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            otpBuilder.append(random.nextInt(10));
        }
        return otpBuilder.toString();
    }

    private boolean checkOtp(String otpCode) {
        return otp!=null && !otp.isExpired() && otpCode.equals(this.otp.getOtpCode());
    }
}
