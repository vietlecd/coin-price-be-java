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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSender emailSender;

    @GetMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(HttpServletRequest req, HttpServletResponse res) {
        try {
            String username = req.getAttribute("username").toString();
            userData userData = userRepository.findByUsername(username);

            emailSender.sendEmail(userData.getEmail(), "test title", "test content");
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "200",
                            "Gửi email thành công, check thư tại, " + userData.getEmail() + "",
                            "/api/forgetPassword"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "200",
                            "Lỗi không xác định",
                            "/api/forgetPassword"),
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
