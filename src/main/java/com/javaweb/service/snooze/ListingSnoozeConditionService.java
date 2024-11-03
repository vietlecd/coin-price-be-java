package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.ListingSnoozeCondition;
import com.javaweb.repository.ListingSnoozeConditionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ListingSnoozeConditionService {

    private final ListingSnoozeConditionRepository listingSnoozeConditionRepository;
    private final ListingSnoozeCondition listingSnoozeCondition;

    // Method to create a new snooze condition
    public ListingSnoozeCondition createSnoozeCondition(ListingSnoozeCondition listingSnoozeCondition, String username) {
        // Set the username in the listingSnoozeCondition object
        listingSnoozeCondition.setUsername(username);

        // Check if a snooze condition already exists for the given symbol and username
        Optional<ListingSnoozeCondition> existingSnoozeCondition = listingSnoozeConditionRepository
                .findBySymbolAndUsername(listingSnoozeCondition.getSymbol(), listingSnoozeCondition.getUsername());

        if (existingSnoozeCondition.isPresent()) {
            // If symbol and username exist together, throw an exception
            throw new IllegalArgumentException("Snooze condition with symbol '"
                    + listingSnoozeCondition.getSymbol() + "' and username '"
                    + listingSnoozeCondition.getUsername() + "' already exists in the database");
        }

        // If no such snooze condition exists, save the new one
        return listingSnoozeConditionRepository.save(listingSnoozeCondition);
    }

    // Method to deactivate (delete) a snooze condition by triggerId
    public void deleteSnoozeCondition(String symbol, String username) {
        Optional<ListingSnoozeCondition> snoozeCondition = listingSnoozeConditionRepository.findBySymbolAndUsername(symbol, username);
        if (snoozeCondition.isPresent()) {
            listingSnoozeConditionRepository.delete(snoozeCondition.get());
        } else {
            throw new RuntimeException("Snooze condition not found for symbol: " + symbol);
        }
    }
}
