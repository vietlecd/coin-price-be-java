package com.javaweb.helpers.trigger;

import com.javaweb.dto.PriceDTO;
import com.javaweb.model.trigger.FuturePriceTrigger;
import com.javaweb.model.trigger.SpotPriceTrigger;
import com.javaweb.repository.FundingRateTriggerRepository;
import com.javaweb.repository.FuturePriceTriggerRepository;
import com.javaweb.repository.SpotPriceTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class TriggerCheckHelper {
    @Autowired
    private FuturePriceTriggerRepository futurePriceTriggerRepository;
    @Autowired
    private SpotPriceTriggerRepository spotPriceTriggerRepository;
    @Autowired
    private ComparisonHelper comparisonHelper;

    public boolean checkSymbolAndTriggerAlert(List<String> symbols, Map<String, ?> priceDataMap, String type) {
        boolean conditionMet = false;

        for (String symbol : symbols) {
            String currentPrice = "";
            PriceDTO priceDTO = null;

            if ("spot".equals(type)) {
                priceDTO = (PriceDTO) priceDataMap.get("Spot Price: " + symbol);
            } else if ("future".equals(type)) {
                priceDTO = (PriceDTO) priceDataMap.get("Future Price: " + symbol);
            }

            if (priceDTO != null) {
                currentPrice = priceDTO.getPrice();
            }

            if (currentPrice == null || currentPrice.trim().isEmpty()) {
                System.out.println("Current price is null or empty for symbol: " + symbol);
                continue;
            }

            // Kiểm tra điều kiện trigger
            if ("spot".equals(type)) {
                SpotPriceTrigger spotTrigger = spotPriceTriggerRepository.findBySymbol(symbol);
                if (spotTrigger != null) {
                    conditionMet = comparisonHelper.checkSpotPriceCondition(spotTrigger, currentPrice);
                }
            } else if ("future".equals(type)) {
                FuturePriceTrigger futureTrigger = futurePriceTriggerRepository.findBySymbol(symbol);
                if (futureTrigger != null) {
                    conditionMet = comparisonHelper.checkFuturePriceCondition(futureTrigger, currentPrice);
                }
            }

            // Nếu điều kiện được thỏa mãn thì trả về true
            if (conditionMet) {
                System.out.println("Condition met for symbol: " + symbol + " " + type);
                return true;
            }
        }

        // Nếu không có điều kiện nào thỏa mãn thì trả về false
        return conditionMet;
    }
}
