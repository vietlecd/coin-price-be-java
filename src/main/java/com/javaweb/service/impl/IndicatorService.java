package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.converter.IndicatorDTOHelper;
import com.javaweb.dto.IndicatorDTO;

import com.javaweb.helpers.service.DateTimeHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.javaweb.service.IIndicatorService;
import com.javaweb.repository.IndicatorRepository;
import com.javaweb.service.IUserIndicatorService;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IndicatorService implements IIndicatorService {
    private final Map<String, IndicatorDTO> indicatorDataUsers = new ConcurrentHashMap<>();
    private final Map<String, IndicatorDTO> indicatorDataTriggers = new ConcurrentHashMap<>();

    private final IndicatorRepository indicatorRepository = new IndicatorRepository();
    @Autowired
    private IUserIndicatorService userIndicatorService;
    @Override
    public Map<String, IndicatorDTO> getIndicatorData(List<String> symbols, List<String> indicators, int days, String username) {
        Map<String, IndicatorDTO> indicatorDataMap = new HashMap<>();

        for (String symbol : symbols) {
            Map<Long, Double> prices = indicatorRepository.getHistoricalPrices(symbol, days);
            Map<String, Object> values = new HashMap<>();
            for (String indicator : indicators) {
                switch (indicator) {
                    case "MA":
                        values.put(indicator, calculateMA(prices));
                        break;
                    case "EMA":
                        values.put(indicator, calculateEMA(prices));
                        break;
                    case "BOLL":
                        values.put(indicator, calculateBOLL(prices));
                        break;
                    default:
                        String code = userIndicatorService.getCode(username, indicator);
                        if (code != null) {
                            Object indicatorValues = calculateUserIndicator(prices, code, indicator);
                            values.put(indicator, indicatorValues);
                        } else {
                            throw new RuntimeException("Indicator không được hỗ trợ: " + indicator);
                        }
                }
            }
            IndicatorDTO indicatorDTO = new IndicatorDTO.Builder()
                    .symbol(symbol)
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

    private Object calculateUserIndicator(Map<Long, Double> prices, String groovyScript, String indicator) {
        List<Double> priceList = new ArrayList<>(prices.values());
        List<Long> timeList = new ArrayList<>(prices.keySet());
        int size = prices.size();

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("groovy");

        Map<String, Object> variables = new HashMap<>();
        variables.put("prices", priceList);
        variables.put("times", timeList);
        variables.put("size", size);
        variables.put(indicator, null);

        SimpleBindings bindings = new SimpleBindings(variables);

        try {
            engine.eval(groovyScript, bindings);
            return bindings.get(indicator);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thực thi script người dùng");
        }
    }

    @Override
    public void handleIndicatorWebSocketMessage(String symbol, JsonNode data,  boolean isTriggered) {
        JsonNode data1 = data.get(symbol);

        Map<String, Object> values = new HashMap<>();
        JsonNode data2 = data1.get("values");
        values.put("MA", data2.get("MA").asDouble());
        values.put("EMA", data2.get("EMA").asDouble());

        long eventTimeLong = data1.get("eventTime").asLong();

        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);

        IndicatorDTO indicatorDTO = IndicatorDTOHelper.createIndicatorDTO(symbol, values, eventTime);

        if (!isTriggered) {
            indicatorDataUsers.put("Indicator: " + symbol, indicatorDTO);
        }
        else {
            indicatorDataTriggers.put("Indicator: " + symbol, indicatorDTO);
        }
    }

    @Override
    public Map<String, IndicatorDTO> getIndicatorDataUsers() { return indicatorDataUsers; }

    @Override
    public Map<String, IndicatorDTO> getIndicatorDataTriggers(){
        return indicatorDataTriggers;
    }
}
