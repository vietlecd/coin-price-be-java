
package com.javaweb.helpers.trigger;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

import com.javaweb.dto.snooze.SpotSnoozeCondition;
import com.javaweb.dto.snooze.FutureSnoozeCondition;
import com.javaweb.dto.snooze.FundingRateSnoozeCondition;
import com.javaweb.repository.SpotSnoozeConditionRepository;
import com.javaweb.repository.FutureSnoozeConditionRepository;
import com.javaweb.repository.FundingRateSnoozeConditionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SnoozeCheckHelper {

    @Autowired
    private SpotSnoozeConditionRepository spotSnoozeConditionRepository;

    @Autowired
    private FutureSnoozeConditionRepository futureSnoozeConditionRepository;

    @Autowired
    private FundingRateSnoozeConditionRepository fundingRateSnoozeConditionRepository;

    public boolean checkSymbolAndSnooze(List<String> symbols, String type) {
        boolean anyConditionMet = false;

        for (String symbol : symbols) {
            boolean conditionMet = false;

            // Kiểm tra loại snooze dựa trên type
            switch (type) {
                case "spot":
                    conditionMet = checkSpotSnooze(symbol); // Kiểm tra snooze cho spot
                    break;
                /*case "future":
                    conditionMet = checkFutureSnooze(symbol); // Kiểm tra snooze cho future
                    break;
                case "funding-rate":
                    conditionMet = checkFundingRateSnooze(symbol); // Kiểm tra snooze cho funding-rate
                    break;*/
                default:
                    System.out.println("Unknown trigger type: " + type);
            }

            if (conditionMet) {
                System.out.println("Snooze condition met for symbol: " + symbol + " in type: " + type);
                anyConditionMet = true;
            }
        }
        return anyConditionMet;
    }

    public boolean checkSpotSnooze(String symbol) {
        // Find the SpotSnoozeCondition by symbol
        Optional<SpotSnoozeCondition> spotSnoozeConditionOptional = spotSnoozeConditionRepository.findBySymbol(symbol);

        if (spotSnoozeConditionOptional.isPresent()) {
            SpotSnoozeCondition condition = spotSnoozeConditionOptional.get();
            LocalDateTime now = LocalDateTime.now();

            // Check the type of snooze and evaluate its condition
            switch (condition.getSnoozeType()) {
                case "ONE_TIME":
                    // Check if the current time is before the end time
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("Unknown snooze type for symbol: " + symbol);
                        return true;
                    }


                case "ONCE_IN_DURATION":
                    // Check if the current time is within the snooze duration (between start and end time)
                    if (now.isAfter(condition.getStartTime()) && now.isBefore(condition.getEndTime())) {
                        System.out.println("Duration snooze type for symbol: " + symbol);
                        return true;
                    }


                case "SPECIFIC_TIME":
                    // Check if the current time is close to the specific time (within 30 minutes range)
                    LocalDateTime specificTime = LocalDateTime.parse(condition.getSpecificTime());
                    if (now.isBefore(specificTime.plusMinutes(30)) && now.isAfter(specificTime.minusMinutes(30))) {
                        System.out.println("Unknown snooze type for symbol: " + symbol);
                    }
                    return true;


            }
        }

        // If no condition is met, return false
        return false;
    }

}
