package com.javaweb.controller;

import com.javaweb.model.LoginRequest;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CreateToken;
import com.nimbusds.jose.KeyLengthException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/findId")
    public Optional<userData> findId(@RequestBody userData user){
        System.out.println(user);

        return userRepository.findById(user.getId());
    }

    @GetMapping("/findUser")
    public userData findAll(@RequestBody userData user){
        System.out.println(user);

        return userRepository.findByUsername(user.getUsername());
    }

    @GetMapping("/getKey")
    public String getKey() {
        try {
            return CreateToken.createToken("hehe", "123");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getDecode")
    public LoginRequest getDecode() {
        try {
            return CreateToken.decodeToken("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtZW1heSIsInN1YiI6ImhlaGUiLCJwYXNzd29yZCI6IjEyMyIsImV4cCI6MTcyODMyMTUxNX0.lp7ng7rTYZSi1TjZ6xNx4OwGYVcRHzuQinNMRXLwWvQ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
