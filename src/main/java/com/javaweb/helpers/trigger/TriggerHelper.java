package com.javaweb.helpers.trigger;

import com.javaweb.dto.trigger.*;
import com.javaweb.model.*;
import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.model.trigger.PriceDifferenceTrigger;
import com.javaweb.model.trigger.SpotFuturePriceTrigger;
import org.springframework.stereotype.Component;

@Component
public class TriggerHelper {
    public SpotFuturePriceTrigger mapSpotFuturePriceTrigger(SpotFuturePriceTriggerDTO dto) {
        SpotFuturePriceTrigger trigger = new SpotFuturePriceTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setAction(dto.getAction());
        trigger.setComparisonOperator(dto.getComparisonOperator());
        trigger.setSpotPriceThreshold(dto.getSpotPriceThreshold());
        trigger.setFuturePriceThreshold(dto.getFuturePriceThreshold());
        return trigger;
    }

    public PriceDifferenceTrigger mapPriceDifferenceTrigger(PriceDifferenceTriggerDTO dto) {
        PriceDifferenceTrigger trigger = new PriceDifferenceTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setAction(dto.getAction());
        trigger.setComparisonOperator(dto.getComparisonOperator());
        trigger.setPriceDifferenceThreshold(dto.getPriceDifferenceThreshold());
        return trigger;
    }

    public FundingRateTrigger mapFundingRateTrigger(FundingRateTriggerDTO dto) {
        FundingRateTrigger trigger = new FundingRateTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setAction(dto.getAction());
        trigger.setComparisonOperator(dto.getComparisonOperator());
        trigger.setFundingRateThreshold(dto.getFundingRateThreshold());
        trigger.setFundingRateInterval(dto.getFundingRateInterval());
        return trigger;
    }
}
