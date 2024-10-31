package com.javaweb.helpers.trigger;

import com.javaweb.dto.trigger.*;
import com.javaweb.model.trigger.*;
import org.springframework.stereotype.Component;

@Component
public class TriggerMapHelper {

    public SpotPriceTrigger mapSpotPriceTrigger(SpotPriceTriggerDTO dto) {
        SpotPriceTrigger trigger = new SpotPriceTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setNotification_method(dto.getNotification_method());
        trigger.setCondition(dto.getCondition());
        trigger.setSpotPriceThreshold(dto.getPrice());
        return trigger;
    }
    public FuturePriceTrigger mapFuturePriceTrigger(FuturePriceTriggerDTO dto) {
        FuturePriceTrigger trigger = new FuturePriceTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setCondition(dto.getCondition());
        trigger.setCondition(dto.getCondition());
        trigger.setFuturePriceThreshold(dto.getPrice());
        return trigger;
    }

    public PriceDifferenceTrigger mapPriceDifferenceTrigger(PriceDifferenceTriggerDTO dto) {
        PriceDifferenceTrigger trigger = new PriceDifferenceTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setNotification_method(dto.getNotification_method());
        trigger.setCondition(dto.getCondition());
        trigger.setPriceDifferenceThreshold(dto.getPriceDifference());
        return trigger;
    }

    public FundingRateTrigger mapFundingRateTrigger(FundingRateTriggerDTO dto) {
        FundingRateTrigger trigger = new FundingRateTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setNotification_method(dto.getNotification_method());
        trigger.setCondition(dto.getCondition());
        trigger.setFundingRateThreshold(dto.getFundingRate());
        //trigger.setFundingRateInterval(dto.getFundingRateInterval());
        return trigger;
    }

    public IndicatorTrigger mapIndicatorTrigger(IndicatorTriggerDTO dto) {
        IndicatorTrigger trigger = new IndicatorTrigger();
        trigger.setSymbol(dto.getSymbol());
        trigger.setIndicator(dto.getIndicator());
        trigger.setValue(dto.getValue());
        trigger.setCondition(dto.getCondition());
        trigger.setPeriod(dto.getPeriod());
        trigger.setNotification_method(dto.getNotification_method());
        return trigger;
    }
}
