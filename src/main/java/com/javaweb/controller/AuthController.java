package com.javaweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/refreshToken")
    public String refreshToken() {

        System.out.println(System.getenv("PORT"));
        return System.getenv("PORT");
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/res")
    public String res() {
        return "res";
    }
}
