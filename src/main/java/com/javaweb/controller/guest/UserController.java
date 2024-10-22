package com.javaweb.controller.guest;

import com.javaweb.controller.AuthController;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.EmailSender;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSender emailSender;

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
                            "/auth/changePassword"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "/auth/changePassword"),
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
                            "/auth/changePassword"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "/auth/changeEmail"),
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
