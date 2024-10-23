package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.PriceDifferenceSnoozeCondition;
import com.javaweb.repository.PriceDifferenceSnoozeConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PriceDifferenceSnoozeConditionService {

    @Autowired
    private PriceDifferenceSnoozeConditionRepository priceDifferenceSnoozeConditionRepository;

    // Method to check if snooze is active for a given trigger
    @Autowired
    PriceDifferenceSnoozeCondition priceDifferenceSnoozeCondition;

    // Method to create a new snooze condition
    public PriceDifferenceSnoozeCondition createSnoozeCondition(PriceDifferenceSnoozeCondition priceDifferenceSnoozeCondition, String username) {
        // Set the username in the priceDifferenceSnoozeCondition object
        priceDifferenceSnoozeCondition.setUsername(username);

        // Check if a snooze condition already exists for the given symbol and username
        Optional<PriceDifferenceSnoozeCondition> existingSnoozeCondition = priceDifferenceSnoozeConditionRepository
                .findBySymbolAndUsername(priceDifferenceSnoozeCondition.getSymbol(), priceDifferenceSnoozeCondition.getUsername());

        if (existingSnoozeCondition.isPresent()) {
            // If symbol and username exist together, throw an exception
            throw new IllegalArgumentException("Snooze condition with symbol '"
                    + priceDifferenceSnoozeCondition.getSymbol() + "' and username '"
                    + priceDifferenceSnoozeCondition.getUsername() + "' already exists in the database");
        }

        // If no such snooze condition exists, save the new one
        return priceDifferenceSnoozeConditionRepository.save(priceDifferenceSnoozeCondition);
    }

    // Method to deactivate (delete) a snooze condition by triggerId
    public void deleteSnoozeCondition(String symbol, String username) {
        Optional<PriceDifferenceSnoozeCondition> snoozeCondition = priceDifferenceSnoozeConditionRepository.findBySymbolAndUsername(symbol, username);
        if (snoozeCondition.isPresent()) {
            priceDifferenceSnoozeConditionRepository.delete(snoozeCondition.get());
        } else {
            throw new RuntimeException("Price difference snooze condition not found for symbol: " + symbol);
        }
    }

    }

