package com.javaweb.controller;

import com.javaweb.model.PriceDTO;
import com.javaweb.service.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DataStorageController {

    @Autowired
    private DataStorageService dataStorageService;

    //API dung de luu du lieu
    @PostMapping("/api/save")
    public void savePrice(@RequestBody PriceDTO priceDTO) {
        dataStorageService.savePrice(priceDTO);
    }

    @GetMapping("/api/spot-price")
    public PriceDTO getSpotPrices(@RequestParam String symbols) {
        PriceDTO priceDTOList = dataStorageService.getSpotPrice(symbols);
        return priceDTOList;
    }
}
