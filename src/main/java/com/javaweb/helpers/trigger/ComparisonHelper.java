package com.javaweb.helpers.trigger;

import com.javaweb.model.trigger.*;
import com.javaweb.service.impl.IndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ComparisonHelper {
    @Autowired
    private IndicatorService indicatorService;

    private void CheckString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Current spot price cannot be null or empty");
        }
    }

    // Kiểm tra Spot Price Trigger
    public boolean checkSpotPriceCondition(SpotPriceTrigger trigger, String currentSpotPriceStr) {
        CheckString(currentSpotPriceStr);

        double currentSpotPrice;
        try {
            currentSpotPrice = Double.parseDouble(currentSpotPriceStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid current future price: must be a valid number", e);
        }

        switch (trigger.getCondition()) {
            case ">=":
                return currentSpotPrice >= trigger.getSpotPriceThreshold();
            case "<":
                return currentSpotPrice < trigger.getSpotPriceThreshold();
            default:
                throw new IllegalArgumentException("Invalid comparison operator for spot price");
        }
    }

    // Kiểm tra Future Price Trigger
    public boolean checkFuturePriceCondition(FuturePriceTrigger trigger, String currentFuturePriceStr) {
        CheckString(currentFuturePriceStr);

        double currentFuturePrice;
        try {
            currentFuturePrice = Double.parseDouble(currentFuturePriceStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid current future price: must be a valid number", e);
        }

        switch (trigger.getCondition()) {
            case ">=":
                return currentFuturePrice >= trigger.getFuturePriceThreshold();
            case "<":
                return currentFuturePrice < trigger.getFuturePriceThreshold();
            default:
                throw new IllegalArgumentException("Invalid comparison operator for future price");
        }
    }

    // Kiểm tra Funding Rate Trigger
    public boolean checkFundingRateCondition(FundingRateTrigger trigger, String currentFundingRateStr) {
        CheckString(currentFundingRateStr);
        double currentFundingRate;
        try {
            currentFundingRate = Double.parseDouble(currentFundingRateStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid current spot price: must be a valid number", e);
        }

        switch (trigger.getCondition()) {
            case ">=":
                return currentFundingRate >= trigger.getFundingRateThreshold();
            case "<":
                return currentFundingRate < trigger.getFundingRateThreshold();
            default:
                throw new IllegalArgumentException("Invalid comparison operator for funding rate");
        }
    }

    // Kiểm tra Indicator Trigger
    public boolean checkMAAndEMACondition(IndicatorTrigger trigger, double currentIndicatorValue) {
        switch (trigger.getCondition()) {
            case ">=":
                indicatorService.handleComparedIndicatorValue(trigger.getSymbol(), trigger.getIndicator(), trigger.getPeriod(), currentIndicatorValue);
                return currentIndicatorValue >= trigger.getValue();
            case "<":
                indicatorService.handleComparedIndicatorValue(trigger.getSymbol(), trigger.getIndicator(), trigger.getPeriod(), currentIndicatorValue);
                return currentIndicatorValue < trigger.getValue();
            default:
                throw new IllegalArgumentException("Invalid comparison operator for funding rate");
        }
    }
    public boolean checkBOLLCondition(IndicatorTrigger trigger, Map<String, Double> currentIndicatorValue) {
        switch (trigger.getCondition()) {
            case ">=":
                indicatorService.handleComparedIndicatorValue(trigger.getSymbol(), trigger.getIndicator() + "Upper", trigger.getPeriod(), currentIndicatorValue.get("UpperBand"));
                return currentIndicatorValue.get("UpperBand") >= trigger.getValue();
            case "<":
                indicatorService.handleComparedIndicatorValue(trigger.getSymbol(), trigger.getIndicator() + "Lower", trigger.getPeriod(), currentIndicatorValue.get("LowerBand"));
                return currentIndicatorValue.get("LowerBand") < trigger.getValue();
            default:
                throw new IllegalArgumentException("Invalid comparison operator for funding rate");
        }
    }

    public boolean checkPriceDifference(PriceDifferenceTrigger trigger, String currentSpotPriceStr, String currentFuturePriceStr) {
        CheckString(currentSpotPriceStr);
        CheckString(currentFuturePriceStr);

        double currentSpotPrice;
        double currentFuturePrice;

        try {
            currentSpotPrice = Double.parseDouble(currentSpotPriceStr);
            currentFuturePrice = Double.parseDouble(currentFuturePriceStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid current spot price: must be a valid number", e);
        }

        double priceDifference = Math.abs(currentSpotPrice - currentFuturePrice);

        return priceDifference > trigger.getPriceDifferenceThreshold();
    }
}
