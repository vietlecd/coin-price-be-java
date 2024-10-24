package com.javaweb.helpers.trigger;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.IndicatorDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.model.trigger.*;
import com.javaweb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public List<String> checkCompareSymbolndTriggerAlert(List<String> symbols, Map<String, PriceDTO> spotPriceDataMap, Map<String, PriceDTO> futurePriceDataMap, String username) {
        List<String> firedSymbols = new ArrayList<>();

        for (String symbol : symbols) {
            PriceDifferenceTrigger trigger = priceDifferenceTriggerRepository.findBySymbolAndUsername(symbol, username);

            if (trigger == null) {
                System.out.println("no trigger found for symbol: " + symbol + " with username " + username);
                continue;
            }

            String spotPrice = getCurrentPrice(symbol, spotPriceDataMap, "Spot");
            String futurePrice = getCurrentPrice(symbol, futurePriceDataMap, "Future");


            if (spotPrice != null && futurePrice != null) {
                boolean conditionMet = comparisonHelper.checkPriceDifference(trigger, spotPrice, futurePrice);

                if (conditionMet) {
                    System.out.println("Price difference exceeds threshold for symbol: " + symbol);
                    firedSymbols.add(symbol);
                } else {
                    System.out.println("No significant price difference for symbol: " + symbol);
                }
            } else {
                System.out.println("Missing price data for symbol: " + symbol);
            }
        }
        return firedSymbols;
    }

    public List<String> checkSymbolAndTriggerAlert(List<String> symbols, Map<String, ?> priceDataMap, String type, String username) {
        List<String> firedSymbols = new ArrayList<>();


        for (String symbol : symbols) {
            boolean conditionMet = false;

            // Lấy giá hiện tại dựa trên loại trigger
            String currentPrice = getCurrentPrice(symbol, priceDataMap, type);
            String currentFundingRate = getCurrentFundingRate(symbol, priceDataMap);

            if ("Spot".equals(type) || "Future".equals(type)) {
                if (currentPrice == null || currentPrice.trim().isEmpty()) {
                    System.out.println("Current price is null or empty for symbol: " + symbol);
                    continue;
                }
            }

            if("ComparePrice".equals(type)) {
                if (currentPrice == null || currentPrice.trim().isEmpty()) {
                    System.out.println("Current price is null or empty for symbol: " + symbol);
                    continue;
                }
            }


            if ("FundingRate".equals(type) && (currentFundingRate == null || currentFundingRate.trim().isEmpty())) {
                System.out.println("Current funding rate is null or empty for symbol: " + symbol);
                continue;
            }

            // Kiểm tra điều kiện trigger cho từng loại
            switch (type) {
                case "Spot":
                    conditionMet = checkSpotTrigger(symbol, currentPrice, username);
                    break;
                case "Future":
                    conditionMet = checkFutureTrigger(symbol, currentPrice, username);
                    break;
                case "FundingRate":
                    conditionMet = checkFundingRateTrigger(symbol, currentFundingRate, username);
                    break;
                default:
                    System.out.println("Unknown trigger type: " + type);
            }

            // Nếu điều kiện được thỏa mãn thì in ra log và đánh dấu kết quả
            if (conditionMet) {
                firedSymbols.add(symbol);
            }
        }

        return firedSymbols;
    }

    public List<String> checkIndicatorSymbolsAndTriggerAlert(List<String> symbols, Map<String, ?> indicatorDataMap, String username) {
        List<String> firedSymbols = new ArrayList<>();


        for (String symbol : symbols) {
            boolean conditionMet = false;

            // Lấy giá trị indicator hiện tại
            String currentIndicatorValue = getCurrentIndicatorValue(symbol, indicatorDataMap);

                if (currentIndicatorValue == null || currentIndicatorValue.trim().isEmpty()) {
                System.out.println("Current price is null or empty for symbol: " + symbol);
                continue;
            }

            // Kiểm tra điều kiện trigger
            conditionMet = checkSpotTrigger(symbol, currentIndicatorValue, username);

            // Nếu điều kiện được thỏa mãn thì in ra log và đánh dấu kết quả
            if (conditionMet) {
                firedSymbols.add(symbol);
            }
        }

        return firedSymbols;
    }

    private String getCurrentPrice(String symbol, Map<String, ?> priceDataMap, String type) {
        PriceDTO priceDTO = null;

        switch (type) {
            case "Spot":
                String spotKey = "Spot Price: " + symbol.toUpperCase();
                if (priceDataMap.containsKey(spotKey)) {
                    priceDTO = (PriceDTO) priceDataMap.get(spotKey);
                } else {
                    System.out.println("Key not found: " + spotKey);
                }

                break;
            case "Future":
                String futureKey = "Future Price: " + symbol.toUpperCase();
                if (priceDataMap.containsKey(futureKey)) {
                    priceDTO = (PriceDTO) priceDataMap.get(futureKey);
                } else {
                    System.out.println("Key not found: " + futureKey);
                }

                break;
            default:
                return null;
        }

        return priceDTO != null ? priceDTO.getPrice() : null;
    }

    private String getCurrentIndicatorValue(String symbol, Map<String, ?> indicatorDataMap) {
        IndicatorDTO indicatorDTO = null;

        String spotKey = "Spot Indicator: " + symbol.toUpperCase();
        if (indicatorDataMap.containsKey(spotKey)) {
            indicatorDTO = (IndicatorDTO) indicatorDataMap.get(spotKey);
        } else {
            System.out.println("Key not found: " + spotKey);
        }

        return indicatorDTO != null ? indicatorDTO.getValues().get(symbol).toString() : null;
    }

    private String getCurrentFundingRate(String symbol, Map<String, ?> priceDataMap) {
        FundingRateDTO fundingRateDTO = null;

        String spotKey = "FundingRate Price: " + symbol.toUpperCase();
        if (priceDataMap.containsKey(spotKey)) {
            fundingRateDTO = (FundingRateDTO) priceDataMap.get(spotKey);
        } else {
            System.out.println("Key not found: " + spotKey);
        }

        return fundingRateDTO != null ? fundingRateDTO.getFundingRate() : null;
    }

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

    private boolean checkIndicatorTrigger(String symbol, String currentIndicatorValue, String username) {
        IndicatorTrigger indicatorTrigger = indicatorTriggerRepository.findBySymbolAndUsername(symbol, username);
        if (indicatorTrigger != null) {
            return comparisonHelper.checkIndicatorCondition(indicatorTrigger, currentIndicatorValue);
        }
        return false;
    }
}
