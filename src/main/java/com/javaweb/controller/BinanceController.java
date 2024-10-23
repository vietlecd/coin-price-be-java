package com.javaweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BinanceController {

    @GetMapping("/start")
    public String start() {
        return "WebSocket connection has already started with server startup.";
    }
}
