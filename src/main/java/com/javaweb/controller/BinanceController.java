package com.javaweb.controller;

import com.javaweb.connect.impl.ListingWebSocketService;
import com.javaweb.service.trigger.BinanceTokenListingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class BinanceController {

    private final BinanceTokenListingService tokenService;

    public BinanceController(BinanceTokenListingService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/start")
    public String startWebSocketClient() {
        try {
            URI uri = new URI("wss://stream.binance.com:9443/ws"); // Địa chỉ WebSocket của Binance
            new ListingWebSocketService(uri, tokenService);
            return "WebSocket connection started.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to start WebSocket connection: " + e.getMessage();
        }
    }
}
