package com.javaweb.helpers.trigger;

import com.javaweb.dto.TriggerConditionDTO;

public class TriggerConditionDTOHelper {
    public static TriggerConditionDTO createTriggerCondition(String symbol, double thresholdValue,
                                                             String comparisonOperator, String action) {
        return new TriggerConditionDTO.Builder()
                .setSymbol(symbol)
                .setThresholdValue(thresholdValue)
                .setComparisonOperator(comparisonOperator)
                .setAction(action)
                .build();
    }
}
