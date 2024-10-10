package com.javaweb.helpers.trigger;

import com.javaweb.models.TriggerCondition;
import org.springframework.stereotype.Component;

@Component
public class ComparisonHelper {
    public boolean checkCondition(TriggerCondition trigger, String currentValueStr) {
        double currentValue;
        try {
            currentValue = Double.parseDouble(currentValueStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid current value: must be a valid number", e);
        }

        switch (trigger.getComparisonOperator()) {
            case "greater_than":
                return currentValue > trigger.getThresholdValue();
            case "less_than":
                return currentValue < trigger.getThresholdValue();
            default:
                throw new IllegalArgumentException("Invalid comparison operator");
        }
    }
}
