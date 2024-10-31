package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.IntervalSnoozeCondition;
import com.javaweb.repository.IntervalSnoozeConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class IntervalSnoozeConditionService {

    @Autowired
    private IntervalSnoozeConditionRepository intervalSnoozeConditionRepository;

    // Method to check if snooze is active for a given trigger
    @Autowired
    IntervalSnoozeCondition intervalSnoozeCondition;

    // Method to create a new snooze condition
    public IntervalSnoozeCondition createSnoozeCondition(IntervalSnoozeCondition intervalSnoozeCondition, String username) {
        // Set the username in the intervalSnoozeCondition object
        intervalSnoozeCondition.setUsername(username);

        // Check if a snooze condition already exists for the given symbol and username
        Optional<IntervalSnoozeCondition> existingSnoozeCondition = intervalSnoozeConditionRepository
                .findBySymbolAndUsername(intervalSnoozeCondition.getSymbol(), intervalSnoozeCondition.getUsername());

        if (existingSnoozeCondition.isPresent()) {
            // If symbol and username exist together, throw an exception
            throw new IllegalArgumentException("Snooze condition with symbol '"
                    + intervalSnoozeCondition.getSymbol() + "' and username '"
                    + intervalSnoozeCondition.getUsername() + "' already exists in the database");
        }

        // If no such snooze condition exists, save the new one
        return intervalSnoozeConditionRepository.save(intervalSnoozeCondition);
    }

    // Method to deactivate (delete) a snooze condition by triggerId
    public void deleteSnoozeCondition(String symbol, String username) {
        Optional<IntervalSnoozeCondition> snoozeCondition = intervalSnoozeConditionRepository.findBySymbolAndUsername(symbol, username);
        if (snoozeCondition.isPresent()) {
            intervalSnoozeConditionRepository.delete(snoozeCondition.get());
        } else {
            throw new RuntimeException("Snooze condition not found for symbol: " + symbol);
        }
    }
}
