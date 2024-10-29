package com.javaweb.model.mongo_entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Document(collection = "userData")
public class userData {
    private String username;
    private String name;
    private String password;
    private String email;
    private Integer vip_role;
    private List<String> ip_list;
    private Integer coin;
    private Otp otp;
    private String telegram_id;

    //Overide lên hàm Setter trong lombok

    public void setPassword(String password) throws Exception {
        String usernameRegex = "^[a-zA-Z0-9_]{8,15}$";
        if (!(password != null && password.matches(usernameRegex))) {
            throw new Exception("Định dạng mật khẩu không hợp lệ, 8-15 ký tự");
        }

        this.password = password;
    }

    public void setUsername(String username) throws Exception {
        String usernameRegex = "^[a-zA-Z0-9_]{3,15}$";
        if (!(username != null && username.matches(usernameRegex))) {
            throw new Exception("Định dạng username không hợp lệ, 8-15 ký tự");
        }

        this.username = username;
    }

    public void setEmail(String email) throws Exception {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if(!(email != null && email.matches(emailRegex))) {
            throw new Exception("Định dạng email không hợp lệ");
        }

        this.email = email;
    }

    //Constructor
    public userData(String username, String name, String password, String email, Integer vip_role, List<String> ip_list) throws Exception {
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
        this.setVip_role(vip_role);
        this.setIp_list(ip_list);
        this.name = name;
        this.setOtp(null);
        coin = 0;
    }

    //Service
    public Integer spendingCoin(Integer newVip) throws Exception {
        Integer amountToSpend = 0;
        if(this.vip_role >= newVip)
            throw new Exception("Vip này mua rồi thì không mua lại được!");

        for(Integer i = this.vip_role; i < newVip; i++){
            amountToSpend += 50000;
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        if(amountToSpend > coin)
            throw new Exception("Không đủ tiền để thanh toán, yêu cầu " + numberFormat.format(amountToSpend) + " coin");

        vip_role = newVip;
        coin -= amountToSpend;

        return amountToSpend;
    }

    public void addCoin(Integer coin) throws Exception {
        if(coin < 10000) {
            throw new Exception("Yêu cầu nạp tiền nhiều hơn 10.000VNĐ!");
        }

        this.coin += coin;
    }

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

    public void useOtp(String otpCode) throws Exception {
        if(checkOtp(otpCode)) {
            otp = null;
        }
        else {
            throw new Exception("Otp không tồn tại!");
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
    private boolean checkOtp(String otpCode) throws Exception {
        if(this.otp == null) throw new Exception("Không tìm thấy mã Otp");
        if(this.otp.isExpired()) throw new Exception("Mã Otp đã hết hạn");
        if(!otpCode.equals(this.otp.getOtpCode())) throw new Exception("Sai mã Otp");

        return true;
    }
}
