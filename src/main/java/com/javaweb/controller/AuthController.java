package com.javaweb.controller;

import com.javaweb.model.LoginRequest;
import com.javaweb.model.RegisterRequest;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CreateToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String token = OtherFunct.getCookieValue(request, "token");

        if(token == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không tìm thấy token!");
        }

        LoginRequest loginRequest = CreateToken.decodeToken(token);
        System.out.println(loginRequest);

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        userData userData = userRepository.findByUsername(username);
        List<String> ip_list = userData.getIp_list();

        if(ip_list.contains(LoginFunc.getClientIp(request)) ) {
            LoginFunc.setCookie(username, userData.getPassword(), response);
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Yêu cầu refresh token không hợp lệ vì tài khoản chưa được đăng nhập trên thiết bị này!, ip thiết bị:" + LoginFunc.getClientIp(request));
        }

        return new ResponseEntity<>(
                new Responses(
                        new Date(),
                        "200",
                        "Yêu cầu refresh token thành công!",
                        "auth/refreshToken"),
                HttpStatus.OK);
    }

    @PutMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse res, HttpServletRequest request) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        userData user = userRepository.findByUsername(username);
        LoginFunc.checkUser(user);
        if (user.getPassword().equals(password)) {
            LoginFunc.setCookie(username, password, res);

            if(!user.getIp_list().contains(LoginFunc.getClientIp(request))) {
                user.addIp(LoginFunc.getClientIp(request));

                try {
                    userRepository.deleteByUsername(username);
                    userRepository.save(user);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + LoginFunc.getClientIp(request));
                }
            }

            return new ResponseEntity<>(
                    new Responses(
                            new Date(),
                            "200",
                            "Đăng nhập thành công",
                            "/auth/login"),
                    HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest reg, HttpServletRequest req, HttpServletResponse res) {
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
    }

    @GetMapping("/logOut")
    public ResponseEntity<?> logout(HttpServletResponse res, HttpServletRequest req) {
        String token = OtherFunct.getCookieValue(req, "token");
        if(token == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy token trong cookie");
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

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", "200");
        response.put("message", "Đăng xuất thành công");
        response.put("path", "/auth/logOut");

        return new ResponseEntity<>(response, HttpStatus.OK);
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
