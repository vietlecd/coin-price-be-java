package com.javaweb.controller;

import com.javaweb.model.PriceDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class APIController {
    private final Map<String, PriceDTO> priceDataMap = new ConcurrentHashMap<>();

    @GetMapping("/api/spot-price")
    public PriceDTO getSpotPrices(@RequestParam String symbol) {
        PriceDTO priceDTO = priceDataMap.get(symbol.toUpperCase());

        if (priceDTO == null) {
            return new PriceDTO(symbol.toUpperCase(), "Price not available");
        }

        return priceDTO;
    }

    public void updatePriceData(String symbol, String price) {
        // Update the in-memory price map
        priceDataMap.put(symbol.toUpperCase(), new PriceDTO(symbol, price));
    }
}
