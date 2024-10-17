package com.javaweb.helpers.trigger;

import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.model.trigger.FuturePriceTrigger;
import com.javaweb.model.trigger.SpotPriceTrigger;
import com.javaweb.repository.FundingRateTriggerRepository;
import com.javaweb.repository.FuturePriceTriggerRepository;
import com.javaweb.repository.SpotPriceTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TriggerCheckHelper {
    @Autowired
    private FuturePriceTriggerRepository futurePriceTriggerRepository;
    @Autowired
    private SpotPriceTriggerRepository spotPriceTriggerRepository;
    @Autowired
    private FundingRateTriggerRepository fundingRateTriggerRepository;
    @Autowired
    private ComparisonHelper comparisonHelper;

    public List<String> checkSymbolAndTriggerAlert(List<String> symbols, Map<String, ?> priceDataMap, String type, String username) {
        List<String> firedSymbols = new ArrayList<>();


        for (String symbol : symbols) {
            boolean conditionMet = false;

            // Lấy giá hiện tại dựa trên loại trigger
            String currentPrice = getCurrentPrice(symbol, priceDataMap, type);
            String currentFundingRate = getCurrentFundingRate(symbol, priceDataMap, type);

            if ("spot".equals(type) || "future".equals(type)) {
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
                case "spot":
                    conditionMet = checkSpotTrigger(symbol, currentPrice, username);
                    break;
                case "future":
                    conditionMet = checkFutureTrigger(symbol, currentPrice);
                    break;
                case "FundingRate":
                    conditionMet = checkFundingRateTrigger(symbol, currentFundingRate);
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

    // Phương thức lấy giá hiện tại dựa trên loại trigger
    private String getCurrentPrice(String symbol, Map<String, ?> priceDataMap, String type) {
        PriceDTO priceDTO = null;

        switch (type) {
            case "spot":
                String key = "Spot Price: " + symbol.toUpperCase();
                if (priceDataMap.containsKey(key)) {
                    priceDTO = (PriceDTO) priceDataMap.get(key);
                } else {
                    System.out.println("Key not found: " + key);
                }

                break;
            case "future":
                priceDTO = (PriceDTO) priceDataMap.get("Future Price: " + symbol);
                break;
            default:
                return null;
        }

        return priceDTO != null ? priceDTO.getPrice() : null;
    }

    private String getCurrentFundingRate(String symbol, Map<String, ?> priceDataMap, String type) {
        FundingRateDTO fundingRateDTO = null;

        if ("FundingRate".equals(type)) {
            fundingRateDTO = (FundingRateDTO) priceDataMap.get("FundingRate Price: " + symbol);
        }

        return fundingRateDTO != null ? fundingRateDTO.getFundingRate() : null;
    }

    // Phương thức kiểm tra trigger cho Spot
    private boolean checkSpotTrigger(String symbol, String currentPrice, String username) {
        SpotPriceTrigger spotTrigger = spotPriceTriggerRepository.findBySymbolAndUsername(symbol, username);
        if (spotTrigger != null) {
            return comparisonHelper.checkSpotPriceCondition(spotTrigger, currentPrice);
        }
        return false;
    }

    // Phương thức kiểm tra trigger cho Future
    private boolean checkFutureTrigger(String symbol, String currentPrice) {
        FuturePriceTrigger futureTrigger = futurePriceTriggerRepository.findBySymbol(symbol);
        if (futureTrigger != null) {
            return comparisonHelper.checkFuturePriceCondition(futureTrigger, currentPrice);
        }
        return false;
    }

    private boolean checkFundingRateTrigger(String symbol, String currentFundingRate) {
        FundingRateTrigger fundingRateTrigger = fundingRateTriggerRepository.findBySymbol(symbol);
        if (fundingRateTrigger != null) {
            return comparisonHelper.checkFundingRateCondition(fundingRateTrigger, currentFundingRate);
        }
        return false;
    }
}
