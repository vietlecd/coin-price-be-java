package com.javaweb.controller;

import com.javaweb.model.LoginRequest;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CreateToken;
import com.nimbusds.jose.KeyLengthException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/decode")
    public LoginRequest decode(HttpServletRequest request) {
        String token = getCookieValue(request, "token"); // Lấy giá trị token từ cookie
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không tìm thấy token!");
        }
        return CreateToken.decodeToken(token); // Giải mã token thành đối tượng LoginRequest
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

}
