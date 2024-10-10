package com.javaweb.controller;

import com.javaweb.model.LoginRequest;
import com.javaweb.model.RegisterRequest;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CreateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/refreshToken")
    public String refreshToken() {
        return "test";
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse res) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        userData user = userRepository.findByUsername(username);

        if(user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu");
        }

        if (user.getPassword().equals(password)) {
            String token = CreateToken.createToken(username, password);
            user.setToken(token);
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", new Date());
            response.put("status", "200");
            response.put("message", "Đăng nhập thành công");
            response.put("path", "/auth/login");

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 13);

            res.addCookie(cookie);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu");
    }

    @GetMapping("/resgister")
    public void register(@RequestBody RegisterRequest req, HttpServletResponse res) {
        if(req.getName() == null || req.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username required!");
        }
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
}
