package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.converter.FundingIntervalDTOHelper;
import com.javaweb.service.IFundingIntervalDataService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class FundingIntervalDataService implements IFundingIntervalDataService {

    private final Cache<String, FundingIntervalDTO> fundingIntervalCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

    @Override
    public Map<String, FundingIntervalDTO> processFundingIntervalData(JsonNode fundingIntervalData) {
        System.out.println("Processing funding interval data for: " + fundingIntervalData);
        Map<String, FundingIntervalDTO> fundingDataMap = new HashMap<>();

        if (fundingIntervalData != null) {
            String symbol = fundingIntervalData.get("symbol").asText();
            String adjustedFundingRateCap = fundingIntervalData.get("adjustedFundingRateCap").asText();
            String adjustedFundingRateFloor = fundingIntervalData.get("adjustedFundingRateFloor").asText();
            Long fundingIntervalHours = fundingIntervalData.get("fundingIntervalHours").asLong();

            FundingIntervalDTO fundingIntervalDTO = FundingIntervalDTOHelper.createFundingRateDTO(symbol,adjustedFundingRateCap,adjustedFundingRateFloor,fundingIntervalHours);

            System.out.println("Storing data in cache for symbol: " + symbol);
            fundingDataMap.put(symbol, fundingIntervalDTO);
            fundingIntervalCache.put(symbol, fundingIntervalDTO); // Cache the result
        } else {
            System.out.println("No valid funding interval data found.");
        }

        return fundingDataMap;
    }

    // Method to check cache before calling API
    @Override
    public Map<String, FundingIntervalDTO> processFundingIntervalDataFromCache(String symbol) {
        System.out.println("Checking cache for symbol: " + symbol);
        FundingIntervalDTO cachedData = fundingIntervalCache.getIfPresent(symbol);
        if (cachedData != null) {
            System.out.println("Cache hit for symbol: " + symbol);
            Map<String, FundingIntervalDTO> fundingDataMap = new HashMap<>();
            fundingDataMap.put(symbol, cachedData);
            return fundingDataMap;
        }
        System.out.println("Cache miss for symbol: " + symbol);
        return null; // Return null if no data in cache
    }
}
