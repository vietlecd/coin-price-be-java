//package com.javaweb.service.snooze;
//
//import com.javaweb.dto.snooze.FundingRateSnoozeCondition;
//import com.javaweb.repository.FundingRateSnoozeConditionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Service
//public class FundingRateSnoozeConditionService {
//
//    @Autowired
//    private FundingRateSnoozeConditionRepository fundingRateSnoozeConditionRepository;
//
//    // Method to check if snooze is active for a given trigger
//    @Autowired
//    FundingRateSnoozeCondition fundingRateSnoozeCondition;
//
//    // Method to create a new snooze condition
//    public FundingRateSnoozeCondition createSnoozeCondition(FundingRateSnoozeCondition fundingRateSnoozeCondition, String username) {
//        // Set the username in the fundingRateSnoozeCondition object
//        fundingRateSnoozeCondition.setUsername(username);
//
//        // Check if a snooze condition already exists for the given symbol and username
//        Optional<FundingRateSnoozeCondition> existingSnoozeCondition = fundingRateSnoozeConditionRepository
//                .findBySymbolAndUsername(fundingRateSnoozeCondition.getSymbol(), fundingRateSnoozeCondition.getUsername());
//
//        if (existingSnoozeCondition.isPresent()) {
//            // If symbol and username exist together, throw an exception
//            throw new IllegalArgumentException("Snooze condition with symbol '"
//                    + fundingRateSnoozeCondition.getSymbol() + "' and username '"
//                    + fundingRateSnoozeCondition.getUsername() + "' already exists in the database");
//        }
//
//        // If no such snooze condition exists, save the new one
//        return fundingRateSnoozeConditionRepository.save(fundingRateSnoozeCondition);
//    }
//
//    // Method to deactivate (delete) a snooze condition by triggerId
//    public void deleteSnoozeConditionByTriggerId(String triggerId) {
//        Optional<FundingRateSnoozeCondition> snoozeCondition = fundingRateSnoozeConditionRepository
//                .findBySymbolAndUsername(fundingRateSnoozeCondition.getSymbol(), fundingRateSnoozeCondition.getUsername());
//        snoozeCondition.ifPresent(condition -> fundingRateSnoozeConditionRepository.delete(condition));
//    }
//}
