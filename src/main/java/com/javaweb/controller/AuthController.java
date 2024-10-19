package com.javaweb.controller;

import com.javaweb.model.LoginRequest;
import com.javaweb.model.RegisterRequest;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CreateToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
//import java.util.List;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String token = getCookieValue(request, "token");

        if(token == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không tìm thấy token!");
        }

        LoginRequest loginRequest = CreateToken.decodeToken(token);
        System.out.println(loginRequest);

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        userData userData = userRepository.findByUsername(username);
        List<String> ip_list = userData.getIp_list();

        if(ip_list.contains( LoginFunc.getClientIp(request)) ) {
            LoginFunc.setCookie(username, userData.getPassword(), response);
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Yêu cầu refresh token không hợp lệ vì tài khoản chưa được đăng nhập trên thiết bị này!");
        }

        return new ResponseEntity<>(
                new Responses(
                        new Date(),
                        "200",
                        "Yêu cầu refresh token thành công!",
                        "auth/refreshToken"),
                HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse res, HttpServletRequest request) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

//        if (userRepository.existsByUsername(username)) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên người dùng đã tồn tại");
//        }

        userData user = userRepository.findByUsername(username);
        LoginFunc.checkUser(user);
        if (user.getPassword().equals(password)) {
            LoginFunc.setCookie(username, password, res);

//            if(!user.getIp_list().contains(LoginFunc.getClientIp(request))) {
//                List<String> ip_list = user.getIp_list();
//                ip_list.add(LoginFunc.getClientIp(request));
//                user.setIp_list(ip_list);
//
//                try {
//                    userRepository.save(user);
//                } catch (Exception e) {
//                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lưu thông tin người dùng");
//                }
//            }

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
    public ResponseEntity<?> logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", "200");
        response.put("message", "Đăng xuất thành công");
        response.put("path", "/auth/logOut");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    static class RegisterFunc {
        static void checkUserAndEmail(userData userData, UserRepository userRepository) {
            if(checkUser(userData.getUsername(), userRepository)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username này đã được đăng kí!");
            }

            if(checkEmail(userData.getEmail(), userRepository)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email này đã được đăng kí!");
            }
        }

        static void bodyInformationCheck(@RequestBody RegisterRequest req) {
            if(req.getName() == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa nhập tên");
            }

            if(req.getEmail() == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa nhập địa chỉ Email");
            }

            if(req.getUsername() == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa nhập Username");
            }

            if(req.getPassword() == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa nhập Password");
            }
        }

        static boolean checkUser(String user, UserRepository userRepository) {
            return userRepository.findByUsername(user) != null;
        }

        static boolean checkEmail(String email, UserRepository userRepository) {
            return userRepository.findByEmail(email) != null;
        }
    }
    static class LoginFunc {
        static String getClientIp(HttpServletRequest request) {
            String ip = request.getHeader("x-forwarded-for");
            if (ip != null && !ip.isEmpty()) {
                ip = ip.split(",")[0];
            }
            else {
                ip = request.getRemoteAddr();
            }

            return ip;
        }

        static void checkUser(userData user) {
            if(user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu");
            }
        }
        static void setCookie(String username, String password, HttpServletResponse res) {
            String token = CreateToken.createToken(username, password);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 13);

            res.addCookie(cookie);
        }
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
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
