package com.javaweb.controller;

import com.javaweb.model.LoginRequest;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public userData login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        System.out.println(username);

        userData data = userRepository.findByUsername(username);

        System.out.println(data);

        return data;
    }

    @GetMapping("/res")
    public String res() {
        return "res";
    }
}
