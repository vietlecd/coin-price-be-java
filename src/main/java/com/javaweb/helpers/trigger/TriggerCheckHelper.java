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
    private FundingRateTriggerRepository fundingRateTriggerRepository;

    @Autowired
    private ComparisonHelper comparisonHelper;

    public void checkSymbolAndTriggerAlert(List<String> symbols, Map<String, ?> priceDataMap, String type, SseEmitter sseEmitter) {
        for (String symbol: symbols) {
            boolean conditionMet = false;

            String currentPrice = "";
            PriceDTO priceDTO = (PriceDTO) priceDataMap.get("Spot Price: " + symbol);

            if (priceDTO != null) {
                currentPrice = priceDTO.getPrice();
            }

            //System.out.println("Symbol: " + symbol + ", Current Price: " + currentPrice);

            // Kiểm tra nếu currentPrice là null hoặc trống
            if (currentPrice == null || currentPrice.trim().isEmpty()) {
                System.out.println("Current price is null or empty for symbol: " + symbol);
                continue;
            }

            switch (type) {
                case "spot":
                    SpotPriceTrigger spotTrigger = spotPriceTriggerRepository.findBySymbol(symbol);
                    if (spotTrigger != null) {
                        conditionMet = comparisonHelper.checkSpotPriceCondition(spotTrigger, currentPrice);
                    }
                    break;
                case "future":
                    FuturePriceTrigger futureTrigger = futurePriceTriggerRepository.findBySymbol(symbol);
                    if (futureTrigger != null) {
                        conditionMet = comparisonHelper.checkFuturePriceCondition(futureTrigger, currentPrice);
                    }
                    break;
//                case "funding-rate":
//                    FundingRateTrigger fundingRateTrigger = fundingRateTriggerRepository.findBySymbol(symbol);
//                    if (fundingRateTrigger != null) {
//                        conditionMet = comparisonHelper.checkFundingRateCondition(fundingRateTrigger, currentPrice);
//                    }
//                    break;
            }

            // Gửi SSE nếu điều kiện thỏa mãn
            if (conditionMet) {
                try {
                    System.out.println("Vượt ngưỡng dô mua i bro ơi!!!!");
                    sseEmitter.send(SseEmitter.event().name("price-alert").data(type + " price condition met for " + symbol));
                } catch (IOException e) {
                    sseEmitter.completeWithError(e);
                }
            }
        }
    }
}