package com.javaweb.service.impl;

<<<<<<< HEAD
import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.converter.IndicatorDTOHelper;

=======
>>>>>>> parent of 206a011 (Merge pull request #45 from dath-241/developer)
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
import java.util.concurrent.*;

@Service
public class IndicatorService implements IIndicatorService {
    private final Map<String, IndicatorDTO> indicatorDataUsers = new ConcurrentHashMap<>();
    private final Map<String, IndicatorDTO> indicatorDataTriggers = new ConcurrentHashMap<>();

    private final IndicatorRepository indicatorRepository = new IndicatorRepository();
    @Autowired
    private IUserIndicatorService userIndicatorService;
    @Override
    public Map<String, IndicatorDTO> getIndicatorData(List<String> symbols, List<String> indicators, int days, String username) throws TimeoutException {
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
                            try {
                                Object indicatorValues = calculateUserIndicator(prices, code, indicator, 5, TimeUnit.SECONDS);
                                values.put(indicator, indicatorValues);
                            } catch (Exception e) {
                                throw e;
                            }
                        } else {
                            throw new IllegalArgumentException("Indicator không được hỗ trợ: " + indicator);
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

    private Object calculateUserIndicator(Map<Long, Double> prices, String groovyScript, String indicator, long timeout, TimeUnit timeUnit) throws TimeoutException {
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

        Callable<Object> task = () -> {
            engine.eval(groovyScript, bindings);
            return bindings.get(indicator);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Object> future = executor.submit(task);

        try {
            return future.get(timeout, timeUnit);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new TimeoutException("Thời gian thực thi script đã vượt quá giới hạn cho indicator: " + indicator);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thực thi script người dùng cho indicator: " + indicator);
        } finally {
            executor.shutdown();
        }
    }
<<<<<<< HEAD

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
=======
>>>>>>> parent of 206a011 (Merge pull request #45 from dath-241/developer)
}
