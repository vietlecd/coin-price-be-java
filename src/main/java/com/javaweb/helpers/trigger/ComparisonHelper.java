package com.javaweb.helpers.trigger;

import com.javaweb.model.trigger.FuturePriceTrigger;
import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.stereotype.Component;

@Component
public class ComparisonHelper {
    // Kiểm tra Spot Price Trigger
    public boolean checkSpotPriceCondition(SpotPriceTrigger trigger, String currentSpotPriceStr) {
        if (currentSpotPriceStr == null || currentSpotPriceStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Current spot price cannot be null or empty");
        }

        double currentSpotPrice;
        try {
            currentSpotPrice = Double.parseDouble(currentSpotPriceStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid current spot price: must be a valid number", e);
        }

        switch (trigger.getComparisonOperator()) {
            case "greater_than":
                return currentSpotPrice > trigger.getSpotPriceThreshold();
            case "less_than":
                return currentSpotPrice < trigger.getSpotPriceThreshold();
            default:
                throw new IllegalArgumentException("Invalid comparison operator for spot price");
        }
    }

    // Kiểm tra Future Price Trigger
    public boolean checkFuturePriceCondition(FuturePriceTrigger trigger, String currentFuturePriceStr) {
        if (currentFuturePriceStr == null || currentFuturePriceStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Current future price cannot be null or empty");
        }

        double currentFuturePrice;
        try {
            currentFuturePrice = Double.parseDouble(currentFuturePriceStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid current future price: must be a valid number", e);
        }

        switch (trigger.getComparisonOperator()) {
            case "greater_than":
                return currentFuturePrice > trigger.getFuturePriceThreshold();
            case "less_than":
                return currentFuturePrice < trigger.getFuturePriceThreshold();
            default:
                throw new IllegalArgumentException("Invalid comparison operator for future price");
        }
    }
}
