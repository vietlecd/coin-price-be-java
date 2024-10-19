package com.javaweb.service.trigger;


import org.springframework.stereotype.Service;

@Service
public class BinanceTokenListingService {
    public void handleNewToken(String message) {

        System.out.println("Handling new token: " + message);

    }
}

