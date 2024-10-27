package com.javaweb.helpers.trigger;

import com.fasterxml.jackson.databind.JsonNode;
//import com.javaweb.config.WebSocketConfig;
//import com.javaweb.dto.FundingRateDTO;
//import com.javaweb.dto.IndicatorDTO;
//import com.javaweb.dto.PriceDTO;
import com.javaweb.model.trigger.*;
import com.javaweb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class TriggerCheckHelper {
    @Autowired
    private PriceDifferenceTriggerRepository priceDifferenceTriggerRepository;
    @Autowired
    private FuturePriceTriggerRepository futurePriceTriggerRepository;
    @Autowired
    private SpotPriceTriggerRepository spotPriceTriggerRepository;
    @Autowired
    private FundingRateTriggerRepository fundingRateTriggerRepository;
    @Autowired
    private IndicatorTriggerRepository indicatorTriggerRepository;
    @Autowired
    private ComparisonHelper comparisonHelper;

//    public List<String> checkCompareSymbolndTriggerAlert(List<String> symbols, Map<String, PriceDTO> spotPriceDataMap, Map<String, PriceDTO> futurePriceDataMap, String username) {
//        List<String> firedSymbols = new ArrayList<>();
//
//        for (String symbol : symbols) {
//            PriceDifferenceTrigger trigger = priceDifferenceTriggerRepository.findBySymbolAndUsername(symbol, username);
//
//            if (trigger == null) {
//                System.out.println("no trigger found for symbol: " + symbol + " with username " + username);
//                continue;
//            }
//
////            String spotPrice = getCurrentPrice(symbol, spotPriceDataMap, "Spot");
////            String futurePrice = getCurrentPrice(symbol, futurePriceDataMap, "Future");
//
//
//            if (spotPrice != null && futurePrice != null) {
//                boolean conditionMet = comparisonHelper.checkPriceDifference(trigger, spotPrice, futurePrice);
//
//                if (conditionMet) {
//                    System.out.println("Price difference exceeds threshold for symbol: " + symbol);
//                    firedSymbols.add(symbol);
//                } else {
//                    System.out.println("No significant price difference for symbol: " + symbol);
//                }
//            } else {
//                System.out.println("Missing price data for symbol: " + symbol);
//            }
//        }
//        return firedSymbols;
//    }

//    public List<String> checkSymbolAndTriggerAlert(List<String> symbols, Map<String, ?> priceDataMap, String type, String username) {
//        List<String> firedSymbols = new ArrayList<>();
//
//
//        for (String symbol : symbols) {
//            boolean conditionMet = false;
//
//            // Lấy giá hiện tại dựa trên loại trigger
////            String currentPrice = getCurrentPrice(symbol, priceDataMap, type);
////            String currentFundingRate = getCurrentFundingRate(symbol, priceDataMap);
//
//            if ("Spot".equals(type) || "Future".equals(type)) {
//                if (currentPrice == null || currentPrice.trim().isEmpty()) {
//                    System.out.println("Current price is null or empty for symbol: " + symbol);
//                    continue;
//                }
//            }
//
//            if("ComparePrice".equals(type)) {
//                if (currentPrice == null || currentPrice.trim().isEmpty()) {
//                    System.out.println("Current price is null or empty for symbol: " + symbol);
//                    continue;
//                }
//            }
//
//
////            if ("FundingRate".equals(type) && (currentFundingRate == null || currentFundingRate.trim().isEmpty())) {
////                System.out.println("Current funding rate is null or empty for symbol: " + symbol);
////                continue;
////            }
//
//            // Kiểm tra điều kiện trigger cho từng loại
//            switch (type) {
//                case "Spot":
//                    conditionMet = checkSpotTrigger(symbol, currentPrice, username);
//                    break;
//                case "Future":
//                    conditionMet = checkFutureTrigger(symbol, currentPrice, username);
//                    break;
//                case "FundingRate":
////                    conditionMet = checkFundingRateTrigger(symbol, currentFundingRate, username);
//                    break;
//                default:
//                    System.out.println("Unknown trigger type: " + type);
//            }
//
//            // Nếu điều kiện được thỏa mãn thì in ra log và đánh dấu kết quả
//            if (conditionMet) {
//                firedSymbols.add(symbol);
//            }
//        }
//
//        return firedSymbols;
//    }

    public List<String> checkIndicatorSymbolsAndTriggerAlert(List<String> symbols, Map<String, ?> indicatorDataMap, String username) {
        List<String> firedSymbols = new ArrayList<>();


        for (String symbol : symbols) {
            boolean conditionMet = false;

            IndicatorTrigger indicatorTrigger = indicatorTriggerRepository.findBySymbolAndUsername(symbol, username);
            // Kiểm tra điều kiện trigger
            conditionMet = checkIndicatorTrigger(symbol, indicatorTrigger, username);

            // Nếu điều kiện được thỏa mãn thì in ra log và đánh dấu kết quả
            if (conditionMet) {
                firedSymbols.add(symbol);
            }
        }

        return firedSymbols;
    }

//    private String getCurrentPrice(String symbol, Map<String, ?> priceDataMap, String type) {
//        PriceDTO priceDTO = null;
//
//        switch (type) {
//            case "Spot":
//                String spotKey = "Spot Price: " + symbol.toUpperCase();
//                if (priceDataMap.containsKey(spotKey)) {
//                    priceDTO = (PriceDTO) priceDataMap.get(spotKey);
//                } else {
//                    System.out.println("Key not found: " + spotKey);
//                }
//
//                break;
//            case "Future":
//                String futureKey = "Future Price: " + symbol.toUpperCase();
//                if (priceDataMap.containsKey(futureKey)) {
//                    priceDTO = (PriceDTO) priceDataMap.get(futureKey);
//                } else {
//                    System.out.println("Key not found: " + futureKey);
//                }
//
//                break;
//            default:
//                return null;
//        }
//
//        return priceDTO != null ? priceDTO.getPrice() : null;
//    }

//    private String getCurrentIndicatorValue(String symbol, Map<String, ?> indicatorDataMap) {
//        IndicatorDTO indicatorDTO = null;
//
//        String spotKey = "Indicator: " + symbol;
//        if (indicatorDataMap.containsKey(spotKey)) {
//            indicatorDTO = (IndicatorDTO) indicatorDataMap.get(spotKey);
//        } else {
//            System.out.println("Key not found: " + spotKey);
//        }
//
//        return indicatorDTO != null ? indicatorDTO.getValues().get(symbol).toString() : null;
//    }

    private Map<Long, Double> getHistoricalPrices(String symbol, int days) {
        String COINGECKO_API_URL = "https://api.coingecko.com/api/v3";
        String url = COINGECKO_API_URL + "/coins/" + symbol + "/market_chart?vs_currency=usd&days=" + days;
        Map<Long, Double> prices = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            if (response != null && response.has("prices")) {
                JsonNode pricesNode = response.get("prices");

                for (JsonNode priceNode : pricesNode) {
                    prices.put(priceNode.get(0).asLong(), priceNode.get(1).asDouble());
                }
            }
            else {
                throw new RuntimeException("Dữ liệu 'prices' không tồn tại cho symbol: " + symbol);
            }

        } catch (Exception e) {
            throw new RuntimeException("Không tìm thấy dữ liệu giá cho symbol: " + symbol, e);
        }
        return prices;
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

//    private String getCurrentFundingRate(String symbol, Map<String, ?> priceDataMap) {
//        FundingRateDTO fundingRateDTO = null;
//
//        String spotKey = "FundingRate Price: " + symbol.toUpperCase();
//        if (priceDataMap.containsKey(spotKey)) {
//            fundingRateDTO = (FundingRateDTO) priceDataMap.get(spotKey);
//        } else {
//            System.out.println("Key not found: " + spotKey);
//        }
//
//        return fundingRateDTO != null ? fundingRateDTO.getFundingRate() : null;
//    }

    // Phương thức kiểm tra trigger cho Spot, Future.....

    private boolean checkSpotTrigger(String symbol, String currentPrice, String username) {
        SpotPriceTrigger spotTrigger = spotPriceTriggerRepository.findBySymbolAndUsername(symbol, username);
        if (spotTrigger != null) {
            return comparisonHelper.checkSpotPriceCondition(spotTrigger, currentPrice);
        }
        return false;
    }

    // Phương thức kiểm tra trigger cho Future
    private boolean checkFutureTrigger(String symbol, String currentPrice, String username) {
        FuturePriceTrigger futureTrigger = futurePriceTriggerRepository.findBySymbolAndUsername(symbol, username);
        if (futureTrigger != null) {
            return comparisonHelper.checkFuturePriceCondition(futureTrigger, currentPrice);
        }
        return false;
    }

    private boolean checkFundingRateTrigger(String symbol, String currentFundingRate, String username) {
        FundingRateTrigger fundingRateTrigger = fundingRateTriggerRepository.findBySymbolAndUsername(symbol, username);
        if (fundingRateTrigger != null) {
            return comparisonHelper.checkFundingRateCondition(fundingRateTrigger, currentFundingRate);
        }
        return false;
    }

    private boolean checkIndicatorTrigger(String symbol, IndicatorTrigger indicatorTrigger, String username) {
        if (indicatorTrigger != null) {
            Map<Long, Double> historicalPrices = getHistoricalPrices(symbol, indicatorTrigger.getPeriod());
            // Lấy giá trị Indicator hiện tại đồng thời kiểm tra indicator condition
            switch (indicatorTrigger.getIndicator()) {
                case "MA":
                    return comparisonHelper.checkMAAndEMACondition(indicatorTrigger, calculateMA(historicalPrices));
                case "EMA":
                    return comparisonHelper.checkMAAndEMACondition(indicatorTrigger, calculateEMA(historicalPrices));
                case "BOLL":
                    return comparisonHelper.checkBOLLCondition(indicatorTrigger, calculateBOLL(historicalPrices));
                default:
            }
        }
        return false;
    }
}
