package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.PriceDifferenceSnoozeCondition;
import com.javaweb.repository.PriceDifferenceSnoozeConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PriceDifferenceSnoozeConditionService {

    @Autowired
    private PriceDifferenceSnoozeConditionRepository priceDifferenceSnoozeConditionRepository;

    // Method to check if snooze is active for a given trigger
    public boolean isSnoozeActive(String symbol) {
        Optional<PriceDifferenceSnoozeCondition> optionalCondition = priceDifferenceSnoozeConditionRepository.findBySymbol(symbol);

        // Check if the snooze condition exists
        if (optionalCondition.isPresent()) {
            PriceDifferenceSnoozeCondition condition = optionalCondition.get();
            LocalDateTime now = LocalDateTime.now();

            switch (condition.getSnoozeType()) {
                case "ONE_TIME":
                    // If it is a one-time snooze and it's still active
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("Snooze active for symbol: " + symbol + " type: " + condition.getSnoozeType());
                        return true;
                    }
                    break;

                case "ONCE_IN_DURATION":
                    // If snooze is active within a duration
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("Snooze active for symbol: " + symbol + " type: " + condition.getSnoozeType());
                        return true;
                    }
                    break;

                case "SPECIFIC_TIME":
                    // Check specific time for snooze
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalDateTime specificTime = LocalDateTime.parse(condition.getSpecificTime(), formatter)
                            .withYear(now.getYear()).withMonth(now.getMonthValue()).withDayOfMonth(now.getDayOfMonth());

                    // If the current time is within 30 minutes before or after the specific time
                    if (now.isBefore(specificTime.plusMinutes(30)) && now.isAfter(specificTime.minusMinutes(30))) {
                        System.out.println("Snooze active for symbol: " + symbol + " type: " + condition.getSnoozeType());
                        return true;
                    }
                    break;

                default:
                    System.out.println("Unknown snooze type for symbol: " + symbol);
                    break;
            }
        }

        // No active snooze found for the given symbol
        return false;
    }

    // Method to create a new snooze condition
    public PriceDifferenceSnoozeCondition createSnoozeCondition(PriceDifferenceSnoozeCondition priceDifferenceSnoozeCondition) {
        Optional<PriceDifferenceSnoozeCondition> existingSnoozeCondition = priceDifferenceSnoozeConditionRepository.findBySymbol(priceDifferenceSnoozeCondition.getSymbol());

        if (existingSnoozeCondition.isPresent()) {
            // If symbol exists, throw an exception or return a custom response
            throw new IllegalArgumentException("Symbol '" + priceDifferenceSnoozeCondition.getSymbol() + "' already exists in the database");
        }

        // If the symbol doesn't exist, save the new snooze condition
        return priceDifferenceSnoozeConditionRepository.save(priceDifferenceSnoozeCondition);
    }

    // Method to deactivate (delete) a snooze condition by triggerId
    public void deleteSnoozeConditionByTriggerId(String triggerId) {
        Optional<PriceDifferenceSnoozeCondition> snoozeCondition = priceDifferenceSnoozeConditionRepository.findBySymbol(triggerId);
        snoozeCondition.ifPresent(condition -> priceDifferenceSnoozeConditionRepository.delete(condition));
    }
}
