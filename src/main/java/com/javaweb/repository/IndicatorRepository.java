package com.javaweb.repository;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.TimeUnit;

import java.util.HashMap;
import java.util.Map;

public class IndicatorRepository {
    private final Cache<String, Map<Long, Double>> cache;
    private final RestTemplate restTemplate;

    public IndicatorRepository() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
        this.restTemplate = new RestTemplate();
    }
    public Map<Long, Double> getHistoricalPrices(String symbol, int days) {
        String cacheKey = symbol + ":" + days;
        Map<Long, Double> cachedPrices = cache.getIfPresent(cacheKey);
        if (cachedPrices != null) {
            return cachedPrices;
        }

        String COINGECKO_API_URL = "https://api.coingecko.com/api/v3";
        String url = COINGECKO_API_URL + "/coins/" + symbol + "/market_chart?vs_currency=usd&days=" + days;
        Map<Long, Double> prices = new HashMap<>();

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            if (response != null && response.has("prices")) {
                JsonNode pricesNode = response.get("prices");

                for (JsonNode priceNode : pricesNode) {
                    prices.put(priceNode.get(0).asLong(), priceNode.get(1).asDouble());
                }
            } else {
                throw new RuntimeException("Dữ liệu 'prices' không tồn tại cho symbol: " + symbol);
            }
        } catch (Exception e) {
            throw new RuntimeException("Không tìm thấy dữ liệu giá cho symbol: " + symbol, e);
        }

        cache.put(cacheKey, prices);

        return prices;
    }
}
