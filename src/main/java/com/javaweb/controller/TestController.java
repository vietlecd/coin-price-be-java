package com.javaweb.controller;

import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
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

    @GetMapping("findId")
    public Optional<userData> findId(@RequestBody String id){
        return userRepository.findById(id);
    }
}
