package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.controller.MarketCapController;
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
public class MarketCapService implements IMarketCapService {

    @Autowired
    private MarketCapController marketCapController;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Dùng để tạo JSON

    // API từ CoinGecko
    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=";

    // Cập nhật dữ liệu MarketCap và Trading Volume từ REST API mỗi 15 phút
    @Scheduled(fixedRate = 900000)  // Cập nhật mỗi 15 phút
    public List<Map<String, Object>> GetMarketData(List<String> symbols) {
        List<Map<String, Object>> marketDataList = new ArrayList<>(); // Danh sách chứa dữ liệu thị trường cho các symbols

        for (String symbol : symbols) {
            String url = COINGECKO_API_URL + symbol;
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null && response.isArray() && response.size() > 0) {
                JsonNode coinData = response.get(0);
                double marketCap = coinData.get("market_cap").asDouble();
                double tradingVolume = coinData.get("total_volume").asDouble();

                // Tạo một Map chứa dữ liệu của symbol hiện tại
                Map<String, Object> symbolData = new HashMap<>();
                symbolData.put("symbol", symbol);
                symbolData.put("marketCap", marketCap);
                symbolData.put("tradingVolume", tradingVolume);

                // Thêm vào danh sách kết quả
                marketDataList.add(symbolData);
            }
        }

        // Trả về danh sách dữ liệu thị trường dưới dạng JSON
        return marketDataList;
    }
}
