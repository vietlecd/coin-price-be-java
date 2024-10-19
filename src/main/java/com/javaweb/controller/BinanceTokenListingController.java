package com.javaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class BinanceTokenListingController {

    @Autowired
    private BinanceTokenListingService binanceTokenListingService;

    @GetMapping("/start")
    public String startWebSocket() {
        try {
            URI uri = new URI("wss://stream.binance.com:9443/ws"); // Địa chỉ WebSocket của Binance
            binanceTokenListingService.connect(uri);
            return "WebSocket connection started.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error starting WebSocket connection.";
        }
    }
}
