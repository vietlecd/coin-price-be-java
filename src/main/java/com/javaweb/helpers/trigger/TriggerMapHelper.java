package com.javaweb.helpers.trigger;

import com.javaweb.dto.trigger.*;
import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.model.trigger.FuturePriceTrigger;
import com.javaweb.model.trigger.PriceDifferenceTrigger;
import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.stereotype.Component;

@Component
public class TriggerMapHelper {
    public SpotPriceTrigger mapSpotPriceTrigger(SpotPriceTriggerDTO dto) {
        SpotPriceTrigger trigger = new SpotPriceTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setAction(dto.getAction());
        trigger.setComparisonOperator(dto.getComparisonOperator());
        trigger.setSpotPriceThreshold(dto.getSpotPriceThreshold());
        return trigger;
    }
    public FuturePriceTrigger mapFuturePriceTrigger(FuturePriceTriggerDTO dto) {
        FuturePriceTrigger trigger = new FuturePriceTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setAction(dto.getAction());
        trigger.setComparisonOperator(dto.getComparisonOperator());
        trigger.setFuturePriceThreshold(dto.getFuturePriceThreshold());
        return trigger;
    }

    public PriceDifferenceTrigger mapPriceDifferenceTrigger(PriceDifferenceTriggerDTO dto) {
        PriceDifferenceTrigger trigger = new PriceDifferenceTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setAction(dto.getAction());
        trigger.setComparisonOperator(dto.getComparisonOperator());

        double priceDifference = Math.abs(dto.getSpotPrice() - dto.getFuturePrice());
        return trigger;
    }

    public FundingRateTrigger mapFundingRateTrigger(FundingRateTriggerDTO dto) {
        FundingRateTrigger trigger = new FundingRateTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setAction(dto.getAction());
        trigger.setComparisonOperator(dto.getComparisonOperator());
        trigger.setFundingRateThreshold(dto.getFundingRateThreshold());
        //trigger.setFundingRateInterval(dto.getFundingRateInterval());
        return trigger;
    }
}
