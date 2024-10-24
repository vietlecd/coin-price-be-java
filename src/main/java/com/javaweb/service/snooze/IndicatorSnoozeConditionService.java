package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.IndicatorSnoozeCondition;
import com.javaweb.repository.IndicatorSnoozeConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class IndicatorSnoozeConditionService {

    @Autowired
    private IndicatorSnoozeConditionRepository indicatorSnoozeConditionRepository;

    // Method to check if snooze is active for a given trigger
    @Autowired
    IndicatorSnoozeCondition indicatorSnoozeCondition;

    // Method to create a new snooze condition
    public IndicatorSnoozeCondition createSnoozeCondition(IndicatorSnoozeCondition indicatorSnoozeCondition, String username) {
        // Set the usernameId in the indicatorSnoozeCondition object
        indicatorSnoozeCondition.setUsername(username);

        // Check if a snooze condition already exists for the given symbol and usernameId
        Optional<IndicatorSnoozeCondition> existingSnoozeCondition = indicatorSnoozeConditionRepository
                .findBySymbolAndUsername(indicatorSnoozeCondition.getSymbol(), indicatorSnoozeCondition.getUsername());

        if (existingSnoozeCondition.isPresent()) {
            // If symbol and usernameId exist together, throw an exception
            throw new IllegalArgumentException("Snooze condition with symbol '"
                    + indicatorSnoozeCondition.getSymbol() + "' and username '"
                    + indicatorSnoozeCondition.getUsername() + "' already exists in the database");
        }

        // If no such snooze condition exists, save the new one
        return indicatorSnoozeConditionRepository.save(indicatorSnoozeCondition);
    }

    // Method to deactivate (delete) a snooze condition by triggerId
    public void deleteSnoozeCondition(String symbol, String username) {
        Optional<IndicatorSnoozeCondition> snoozeCondition = indicatorSnoozeConditionRepository.findBySymbolAndUsername(symbol, username);
        if (snoozeCondition.isPresent()) {
            indicatorSnoozeConditionRepository.delete(snoozeCondition.get());
        } else {
            throw new RuntimeException("Snooze condition not found for symbol: " + symbol);
        }
    }
}
