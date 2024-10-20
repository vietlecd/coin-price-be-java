package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.service.IMarketCapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarketCapService {

    private final RestTemplate restTemplate = new RestTemplate();

    // API từ CoinGecko
    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=";

    public List<Map<String, Object>> getMarketData(List<String> symbols) {
        List<Map<String, Object>> marketDataList = new ArrayList<>();

        for (String symbol : symbols) {
            String url = COINGECKO_API_URL + symbol;
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null && response.isArray() && response.size() > 0) {
                JsonNode coinData = response.get(0);
                double marketCap = coinData.get("market_cap").asDouble();
                double tradingVolume = coinData.get("total_volume").asDouble();

                Map<String, Object> symbolData = new HashMap<>();
                symbolData.put("symbol", symbol);
                symbolData.put("marketCap", marketCap);
                symbolData.put("tradingVolume", tradingVolume);

                marketDataList.add(symbolData);
            }
        }

        return marketDataList;
    }
}