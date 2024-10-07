package com.javaweb.controller;

import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/getUser/{id}")
    public String getUser(@PathVariable int id) {

        return "test";
    }

    @PostMapping("/postUser")
    public String postUser(@RequestBody userData user) {
        userRepository.save(user);
        return "test";
    }
}
