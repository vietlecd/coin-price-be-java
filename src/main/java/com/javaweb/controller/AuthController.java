package com.javaweb.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final Environment environment;

    public AuthController(Environment environment) {
        this.environment = environment;
    }
//    @Value("${PORT}")
//    private final String PORT;

    @GetMapping("/refreshToken")
    public String refreshToken() {
        return "test";
    }

    @GetMapping("/login")
    public String login() {
//        Dotenv dotenv = Dotenv.load();
        String port = environment.getProperty("port");
        String dbUrl = "hi";

        System.out.println(port);
        return port;
    }

    @GetMapping("/res")
    public String res() {
        return "res";
    }
}
