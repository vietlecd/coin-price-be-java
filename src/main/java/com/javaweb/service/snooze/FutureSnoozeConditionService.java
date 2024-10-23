package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.FutureSnoozeCondition;
import com.javaweb.dto.snooze.SpotSnoozeCondition;
import com.javaweb.repository.FutureSnoozeConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class FutureSnoozeConditionService {

    @Autowired
    private FutureSnoozeConditionRepository futureSnoozeConditionRepository;

    // Method to check if snooze is active for a given trigger
    @Autowired
    FutureSnoozeCondition futureSnoozeCondition;

    // Method to create a new snooze condition
    public FutureSnoozeCondition createFutureSnoozeCondition(FutureSnoozeCondition futureSnoozeCondition, String username) {
        // Set the username in the futureSnoozeCondition object
        futureSnoozeCondition.setUsername(username);

        // Check if a snooze condition already exists for the given symbol and username
        Optional<FutureSnoozeCondition> existingSnoozeCondition = futureSnoozeConditionRepository
                .findBySymbolAndUsername(futureSnoozeCondition.getSymbol(), username);

        if (existingSnoozeCondition.isPresent()) {
            // If symbol and username exist together, throw an exception
            throw new IllegalArgumentException("Snooze condition with symbol '"
                    + futureSnoozeCondition.getSymbol() + "' and username '"
                    + futureSnoozeCondition.getUsername() + "' already exists in the database");
        }

        // If no such snooze condition exists, save the new one
        return futureSnoozeConditionRepository.save(futureSnoozeCondition);
    }

    // Method to deactivate (delete) a snooze condition by triggerId
    public void deleteSnoozeCondition(String symbol, String username) {
        Optional<FutureSnoozeCondition> snoozeCondition = futureSnoozeConditionRepository.findBySymbolAndUsername(symbol, username);
        if (snoozeCondition.isPresent()) {
            futureSnoozeConditionRepository.delete(snoozeCondition.get());
        } else {
            throw new RuntimeException("Future snooze condition not found for symbol: " + symbol);
        }
    }

}
