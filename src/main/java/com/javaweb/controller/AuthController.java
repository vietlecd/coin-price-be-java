package com.javaweb.controller;

import com.javaweb.controller.guest.UserController;
import com.javaweb.model.LoginRequest;
import com.javaweb.model.RegisterRequest;
import com.javaweb.model.mongo_entity.Otp;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CreateToken;
import com.javaweb.service.EmailSender;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.javaweb.service.authServices.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = OtherFunct.getCookieValue(request, "token");

            if (token == null) {
                throw new Exception("Không tìm thấy token!");
            }
            LoginRequest loginRequest = CreateToken.decodeToken(token);

            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            userData userData = userRepository.findByUsername(username);
            List<String> ip_list = userData.getIp_list();

            if (ip_list.contains(LoginFunc.getClientIp(request))) {
                LoginFunc.setCookie(username, userData.getPassword(), response);
            } else {
                throw new Exception("Yêu cầu refresh token không hợp lệ vì tài khoản chưa được đăng nhập trên thiết bị này!, ip thiết bị:" + LoginFunc.getClientIp(request));
            }

            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "200",
                            "Yêu cầu refresh token thành công!",
                            "auth/refreshToken"),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "auth/refreshToken"),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/loginWithEmail")
    public ResponseEntity<?> loginWithEmail(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> info) {
        try {
            String email = info.get("email");
            String password = info.get("password");

            if(email == null || password == null) {
                throw new Exception("Không tìm thấy email hoặc password");
            }

            userData user = userRepository.findByEmail(email);
            if(user == null) {
                throw new Exception("Không tìm thấy user");
            }

            if(user.getPassword().equals(password)) {
                LoginFunc.setCookie(user.getUsername(), password, response);

                if (!user.getIp_list().contains(LoginFunc.getClientIp(request))) {
                    user.addIp(LoginFunc.getClientIp(request));

                    userRepository.deleteByUsername(user.getUsername());
                    userRepository.save(user);
                }

                return new ResponseEntity<>(
                        new Responses(
                                new Date(),
                                "200",
                                "Đăng nhập thành công",
                                "/auth/login"),
                        HttpStatus.OK);
            }
            throw new Exception("Sai email hoặc mật khẩu");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                        new Date(),
                        "400",
                        e.getMessage(),
                        "/auth/loginWithEmail"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse res, HttpServletRequest request) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            userData user = userRepository.findByUsername(username);

            if(user == null)
                throw new Exception("Không tìm thấy username này!");

            if (user.getPassword().equals(password)) {
                LoginFunc.setCookie(username, password, res);

                if (!user.getIp_list().contains(LoginFunc.getClientIp(request))) {
                    user.addIp(LoginFunc.getClientIp(request));

                    userRepository.deleteByUsername(username);
                    userRepository.save(user);
                }

                return new ResponseEntity<>(
                        new Responses(
                                new Date(),
                                "200",
                                "Đăng nhập thành công",
                                "/auth/login"),
                        HttpStatus.OK);
            }

            throw new Exception("Sai tên đăng nhập hoặc mật khẩu");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "/auth/login"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest reg, HttpServletRequest req, HttpServletResponse res) {
        try {
            RegisterFunc.bodyInformationCheck(reg);

            List<String> ip_list = new ArrayList<>();
            ip_list.add(LoginFunc.getClientIp(req));
            userData user = new userData(
                    reg.getUsername(),
                    reg.getName(),
                    reg.getPassword(),
                    reg.getEmail(),
                    0,
                    ip_list
            );
            RegisterFunc.checkUserAndEmail(user, userRepository);

            LoginFunc.setCookie(user.getUsername(), user.getPassword(), res);
            userRepository.save(user);
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "200",
                            "Đăng kí tài khoản thành công",
                            "/auth/logOut"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "/auth/logOut"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/logOut")
    public ResponseEntity<?> logout(HttpServletResponse res, HttpServletRequest req) {
        try {
            String token = OtherFunct.getCookieValue(req, "token");
            if (token == null) {
                throw new Exception("Không tìm thấy token trong cookie");
            }

            Cookie cookie = new Cookie("token", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            res.addCookie(cookie);

            LoginRequest loginRequest = CreateToken.decodeToken(token);
            String username = loginRequest.getUsername();
            String ip = LoginFunc.getClientIp(req);

            userData user = userRepository.findByUsername(username);
            user.removeIp(ip);
            userRepository.deleteByUsername(username);
            userRepository.save(user);

            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "200",
                            "Đăng xuất thành công",
                            "/auth/logOut"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "/auth/logOut"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/forgotPassword")
    public ResponseEntity<?> forgetPassword(@RequestParam String username) {
        try {
            userData userdata = userRepository.findByUsername(username);

            if(userdata == null) {
                throw new Exception("Không có user này");
            }

            emailSender.sendOtp(userdata.getEmail());

            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "200",
                            "Gửi email thành công, check thư tại, " + userdata.getEmail() + "",
                            "/api/forgetPassword"),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "400",
                            e.getMessage(),
                            "/api/forgetPassword"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String otpCode, @RequestBody Map<String, String> params) {
        try {
            String newPassword = params.get("newPassword");
            if(newPassword == null) {
                throw new RuntimeException("Không tìm thấy 'newPassword' trong Body");
            }
            userData user = userRepository.findByEmail(email);
            user.useOtp(otpCode);
            user.setPassword(newPassword);

            userRepository.deleteByUsername(user.getUsername());
            userRepository.save(user);

            emailSender.sendEmail(user.getEmail(),
                    "Phát hện yêu cầu đổi mật khẩu!",
                    "Yêu cầu đổi mật khẩu thành công!");

            return new ResponseEntity<>(
                new Responses(
                        new Date(),
                        "200",
                        "Đổi mật khẩu thành công, vui lòng đăng nhập lại.",
                        "/api/forgetPassword"),
                HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                new Responses(
                    new Date(),
                    "200",
                    e.getMessage(),
                    "/api/forgetPassword")
                ,HttpStatus.BAD_REQUEST
            );
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
