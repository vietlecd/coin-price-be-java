package com.javaweb.controller.guest;

import com.javaweb.controller.AuthController;
import com.javaweb.model.mongo_entity.paymentHistory;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.PaymentRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.EmailSender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSender emailSender;

    @Autowired
    PaymentRepository paymentRepository;

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(HttpServletRequest request,@RequestBody Map<String, String> pass) {
        try {
            String newPassword = pass.get("newPassword");
            if(newPassword==null) {
                throw new Exception("Mật khẩu không hợp lệ");
            }

            String username = request.getAttribute("username").toString();
            userData userData = userRepository.findByUsername(username);
            userData.setPassword(newPassword);
            userRepository.deleteByUsername(username);
            userRepository.save(userData);

            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "200",
                            "Đổi mật khẩu thành công!",
                            "/api/changePassword"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "/api/changePassword"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(HttpServletRequest request, @RequestParam String email) {
        try {
            if(email==null) {
                throw new Exception("Không tìm thấy email");
            }

            String username = request.getAttribute("username").toString();
            userData userData = userRepository.findByUsername(username);
            userData.setEmail(email);
            userRepository.deleteByUsername(username);
            userRepository.save(userData);

            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "200",
                            "Đổi email thành công!",
                            "/api/changeEmail"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "/api/changeEmail"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/deposit")
    public ResponseEntity<?> donateTiTienChoBackend(HttpServletRequest request, @RequestParam Integer amount) {
        try {
            if(amount==null) {
                throw new Exception("Nhập tiền là số nguyên dương lớn hơn 10.000");
            }

            String username = request.getAttribute("username").toString();
            userData userData = userRepository.findByUsername(username);
            userData.addCoin(amount);

            userRepository.deleteByUsername(username);
            userRepository.save(userData);

            paymentRepository.save(new paymentHistory(
                    new Date(),
                    username,
                    userData.getEmail(),
                    amount
            ));

            return new ResponseEntity<>(
                new Responses(
                        new Date(),
                        "200",
                        "Nạp tiền thành công!",
                        "/api/deposit"),
                HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                new Responses(
                        new Date(),
                        "400",
                        e.getMessage(),
                        "/api/deposit"),
                HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/purchaseVip")
    public ResponseEntity<?> purchaseVip(HttpServletRequest request, @RequestParam Integer vipLevel) {
        try {
            if(vipLevel == null || vipLevel<=0 || vipLevel>3) {
                throw new Exception("Không tồn tại vip này");
            }
            String username = request.getAttribute("username").toString();
            userData userData = userRepository.findByUsername(username);
            Integer amount = userData.spendingCoin(vipLevel);

            userRepository.deleteByUsername(username);
            userRepository.save(userData);

            paymentRepository.save(new paymentHistory(
                    new Date(),
                    username,
                    userData.getEmail(),
                    -amount
            ));

            NumberFormat numberFormat = NumberFormat.getInstance();
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "200",
                            "Thanh toán thành công, đã trừ " + numberFormat.format(amount) + "đ trong tài khoản",
                            "/api/purchaseVip"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "/api/purchaseVip"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> info(HttpServletRequest request) {
        try {
            Map<String, Object> info = new HashMap<>();
            String username = request.getAttribute("username").toString();
            userData user = userRepository.findByUsername(username);

            info.put("username", user.getUsername());
            info.put("name", user.getName());
            info.put("email", user.getEmail());
            info.put("coin", user.getCoin());
            info.put("vip_role", user.getVip_role());

            return new ResponseEntity<>(info, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                new Responses(
                        new Date(),
                        "400",
                        e.getMessage(),
                        "/api/info"),
                HttpStatus.BAD_REQUEST);
        }
    }


    @Data
    @AllArgsConstructor
    private class Responses{
        private Date timestamp;
        private String status;
        private String message;
        private String path;
    }
}
