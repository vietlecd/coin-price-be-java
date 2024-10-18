package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.SpotSnoozeCondition;
import com.javaweb.repository.SpotSnoozeConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class SpotSnoozeConditionService {

    @Autowired
    private SpotSnoozeConditionRepository spotSnoozeConditionRepository;

    // Method to check if snooze is active for a given trigger
    @Autowired
    SpotSnoozeCondition spotSnoozeCondition;





    // Method to create a new snooze condition
    public SpotSnoozeCondition createSnoozeCondition(SpotSnoozeCondition spotSnoozeCondition, String usernameId) {
        // Set the usernameId in the spotSnoozeCondition object
        spotSnoozeCondition.setUsernameId(usernameId);

        // Check if a snooze condition already exists for the given symbol and usernameId
        Optional<SpotSnoozeCondition> existingSnoozeCondition = spotSnoozeConditionRepository
                .findBySymbolAndUsernameId(spotSnoozeCondition.getSymbol(), spotSnoozeCondition.getUsernameId());

        if (existingSnoozeCondition.isPresent()) {
            // If symbol and usernameId exist together, throw an exception
            throw new IllegalArgumentException("Snooze condition with symbol '"
                    + spotSnoozeCondition.getSymbol() + "' and usernameId '"
                    + spotSnoozeCondition.getUsernameId() + "' already exists in the database");
        }

        // If no such snooze condition exists, save the new one
        return spotSnoozeConditionRepository.save(spotSnoozeCondition);
    }



    // Method to deactivate (delete) a snooze condition by triggerId
    public void deleteSnoozeConditionByTriggerId(String triggerId) {
        Optional<SpotSnoozeCondition> snoozeCondition = spotSnoozeConditionRepository.findBySymbolAndUsernameId(spotSnoozeCondition.getSymbol(), spotSnoozeCondition.getUsernameId());
        snoozeCondition.ifPresent(condition -> spotSnoozeConditionRepository.delete(condition));
    }
}
