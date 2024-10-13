package com.javaweb.helpers.trigger;

import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.model.trigger.FuturePriceTrigger;
import com.javaweb.model.trigger.PriceDifferenceTrigger;
import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.stereotype.Component;

@Component
public class ComparisonHelper {
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
        CheckString(currentFuturePriceStr);

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

    // Kiểm tra Funding Rate Trigger
    public boolean checkFundingRateCondition(FundingRateTrigger trigger, String currentFundingRateStr) {
        CheckString(currentFundingRateStr);
        double currentFundingRate;
        try {
            currentFundingRate = Double.parseDouble(currentFundingRateStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid current spot price: must be a valid number", e);
        }

        switch (trigger.getComparisonOperator()) {
            case "greater_than":
                return currentFundingRate > trigger.getFundingRateThreshold();
            case "less_than":
                return currentFundingRate < trigger.getFundingRateThreshold();
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
