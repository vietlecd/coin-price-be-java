package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.converter.IndicatorDTOHelper;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.IndicatorDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.service.DateTimeHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.javaweb.service.IIndicatorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class IndicatorService implements IIndicatorService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, IndicatorDTO> indicatorDataUsers = new ConcurrentHashMap<>();
    private final Map<String, IndicatorDTO> indicatorDataTriggers = new ConcurrentHashMap<>();

    @Override
    public Map<String, IndicatorDTO> getIndicatorData(List<String> symbols, List<String> indicators, int days) {
        Map<String, IndicatorDTO> indicatorDataMap = new HashMap<>();

        for (String symbol : symbols) {
            Map<Long, Double> prices = getHistoricalPrices(symbol, days);
            Map<String, Object> values = new HashMap<>();
            for (String indicator : indicators) {
                switch (indicator) {
                    case "MA":
                        values.put("MA", calculateMA(prices));
                        break;
                    case "EMA":
                        values.put("EMA", calculateEMA(prices));
                        break;
                    case "BOLL":
                        values.put("BOLL", calculateBOLL(prices));
                        break;
                    default:
                        throw new RuntimeException("Indicator không được hỗ trợ: " + indicator);
                }
            }
            IndicatorDTO indicatorDTO = new IndicatorDTO.Builder()
                    .values(values)
                    .eventTime(DateTimeHelper.formatEventTime(prices.keySet().stream().max(Long::compareTo).orElse(0L)))
                    .build();
            indicatorDataMap.put(symbol, indicatorDTO);
        }

        return indicatorDataMap;
    }

    private double calculateMA(Map<Long, Double> prices) {
        if (prices.isEmpty()) return 0;
        return prices.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    private double calculateEMA(Map<Long, Double> prices) {
        if (prices.isEmpty()) return 0;

        List<Double> priceList = new ArrayList<>(prices.values());
        double alpha = 2.0 / (prices.size() + 1);
        double ema = priceList.get(0);

        for (int i = 1; i < priceList.size(); i++) {
            double price = priceList.get(i);
            ema = price * alpha + ema * (1 - alpha);
        }

        return ema;
    }

    private Map<String, Double> calculateBOLL(Map<Long, Double> prices) {
        Map<String, Double> bands = new HashMap<>();
        if (prices.isEmpty()) {
            bands.put("MiddleBand", 0.0);
            bands.put("UpperBand", 0.0);
            bands.put("LowerBand", 0.0);
            return bands;
        }

        double ma = calculateMA(prices);

        double standardDeviation = Math.sqrt(prices.values().stream()
                .mapToDouble(price -> Math.pow(price - ma, 2))
                .sum() / prices.size());

        double upperBand = ma + 2 * standardDeviation;
        double lowerBand = ma - 2 * standardDeviation;

        bands.put("MiddleBand", ma);
        bands.put("UpperBand", upperBand);
        bands.put("LowerBand", lowerBand);

        return bands;
    }
    private Map<Long, Double> getHistoricalPrices(String symbol, int days) {
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

        return prices;
    }

    @Override
    public void handleFundingRateWebSocketMessage(JsonNode data,  boolean isTriggered) {
        long eventTimeLong = data.get("E").asLong();
        long nextFundingTime = data.get("T").asLong();

        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);

        String symbol = data.get("s").asText();
        JsonNode data1 = data.get("v");
        Map<String, Object> values = new HashMap<>();
        values.put(data1.get("S").asText(), data1.get("D").asDouble());

        long countdownInSeconds = (nextFundingTime - eventTimeLong) / 1000;

        String fundingCountdown = String.format("%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(countdownInSeconds),
                TimeUnit.SECONDS.toMinutes(countdownInSeconds) % 60,
                countdownInSeconds % 60
        );


        IndicatorDTO indicatorDTO = IndicatorDTOHelper.createIndicatorDTO(symbol, values, eventTime);

        if (!isTriggered) {
            indicatorDataUsers.put("FundingRate Price: " + symbol, indicatorDTO);
        }
        else {
            indicatorDataTriggers.put("FundingRate Price: " + symbol, indicatorDTO);
        }


    }

    @Override
    public Map<String, IndicatorDTO> getIndicatorDataTriggers(){
        return indicatorDataTriggers;
    }
}
