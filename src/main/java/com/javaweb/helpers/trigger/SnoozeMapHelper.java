package com.javaweb.helpers.trigger;

import com.javaweb.dto.snooze.FundingRateSnoozeCondition;
import com.javaweb.dto.snooze.FutureSnoozeCondition;
import com.javaweb.dto.snooze.PriceDifferenceSnoozeCondition;
import com.javaweb.dto.snooze.SpotSnoozeCondition;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class SnoozeMapHelper {
    public SpotSnoozeCondition mapToSpotSnoozeCondition(Map<String, Object> snoozeConditionRequest) {
        // Perform the mapping of the request body to SpotSnoozeCondition
        // For example, using Jackson's ObjectMapper
        return new SpotSnoozeCondition(


                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );
    }

    // Method to map the request body to a FutureSnoozeCondition DTO
    public FutureSnoozeCondition mapToFutureSnoozeCondition(Map<String, Object> snoozeConditionRequest) {
        return new FutureSnoozeCondition(
                (String) snoozeConditionRequest.get("usernameId"),
                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );
    }

    // Method to map the request body to a PriceDifferenceSnoozeCondition DTO
    public PriceDifferenceSnoozeCondition mapToPriceDifferenceSnoozeCondition(Map<String, Object> snoozeConditionRequest) {
        return new PriceDifferenceSnoozeCondition(
                (String) snoozeConditionRequest.get("usernameId"),
                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );
    }

    public FundingRateSnoozeCondition mapToFundingRateSnoozeCondition(Map<String, Object> snoozeConditionRequest) {
        return new FundingRateSnoozeCondition(
                (String) snoozeConditionRequest.get("usernameId"),
                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );
    }

}
