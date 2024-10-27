//package com.javaweb.helpers.trigger;
//
////import com.javaweb.dto.snooze.FundingRateSnoozeCondition;
////import com.javaweb.dto.snooze.FutureSnoozeCondition;
////import com.javaweb.dto.snooze.PriceDifferenceSnoozeCondition;
////import com.javaweb.dto.snooze.SpotSnoozeCondition;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//@Component
//public class SnoozeMapHelper {
//    public SpotSnoozeCondition mapToSpotSnoozeCondition(Map<String, Object> snoozeConditionRequest, String username) {
//        // Perform the mapping of the request body to SpotSnoozeCondition
//        SpotSnoozeCondition spotSnoozeCondition = new SpotSnoozeCondition(
//
//                (String) snoozeConditionRequest.get("symbol"),
//                (String) snoozeConditionRequest.get("conditionType"),
//                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
//                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
//                (String) snoozeConditionRequest.get("specificTime")
//        );
//
//        // Set the username (usernameId) for the SpotSnoozeCondition object
//        if (snoozeConditionRequest.get("repeatCount") != null) {
//            spotSnoozeCondition.setRepeatCount((Integer) snoozeConditionRequest.get("repeatCount"));
//        } else {
//            spotSnoozeCondition.setRepeatCount(0); // default value if not provided
//        }
//
//        if (snoozeConditionRequest.get("maxRepeatCount") != null) {
//            spotSnoozeCondition.setMaxRepeatCount((Integer) snoozeConditionRequest.get("maxRepeatCount"));
//        } else {
//            spotSnoozeCondition.setMaxRepeatCount(1); // default value if not provided
//        }
//
//        // Print out the object after mapping for debugging purposes
//        System.out.println("SpotSnoozeCondition after mapping: " + spotSnoozeCondition);
//        spotSnoozeCondition.setUsername(username);
//        System.out.println("SpotSnoozeCondition after mapping: " + spotSnoozeCondition);
//        return spotSnoozeCondition;
//    }
//
//
//    // Method to map the request body to a FutureSnoozeCondition DTO
////    public FutureSnoozeCondition mapToFutureSnoozeCondition(Map<String, Object> snoozeConditionRequest,String username) {
////        FutureSnoozeCondition futureSnoozeCondition = new FutureSnoozeCondition(
////
////                (String) snoozeConditionRequest.get("symbol"),
////                (String) snoozeConditionRequest.get("conditionType"),
////                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
////                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
////                (String) snoozeConditionRequest.get("specificTime")
////        );
////
////        // Set the username (usernameId) for the SpotSnoozeCondition object
////        futureSnoozeCondition.setUsername(username);
////        System.out.println("SpotSnoozeCondition after mapping: " + futureSnoozeCondition);
////        return futureSnoozeCondition;
////    }
//
////    // Method to map the request body to a PriceDifferenceSnoozeCondition DTO
////    public PriceDifferenceSnoozeCondition mapToPriceDifferenceSnoozeCondition(Map<String, Object> snoozeConditionRequest, String username) {
////        // Perform the mapping of the request body to PriceDifferenceSnoozeCondition
////        PriceDifferenceSnoozeCondition priceDifferenceSnoozeCondition = new PriceDifferenceSnoozeCondition(
////
////                (String) snoozeConditionRequest.get("symbol"),
////                (String) snoozeConditionRequest.get("conditionType"),
////                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
////                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
////                (String) snoozeConditionRequest.get("specificTime")
////        );
////
////        // Set the username (usernameId) for the PriceDifferenceSnoozeCondition object
////        priceDifferenceSnoozeCondition.setUsername(username);
////        System.out.println("PriceDifferenceSnoozeCondition after mapping: " + priceDifferenceSnoozeCondition);
////        return priceDifferenceSnoozeCondition;
////    }
//
//
////    public FundingRateSnoozeCondition mapToFundingRateSnoozeCondition(Map<String, Object> snoozeConditionRequest, String username) {
////        // Perform the mapping of the request body to FundingRateSnoozeCondition
////        FundingRateSnoozeCondition fundingRateSnoozeCondition = new FundingRateSnoozeCondition(
////
////                (String) snoozeConditionRequest.get("symbol"),
////                (String) snoozeConditionRequest.get("conditionType"),
////                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
////                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
////                (String) snoozeConditionRequest.get("specificTime")
////        );
////
////        // Set the username (usernameId) for the FundingRateSnoozeCondition object
////        fundingRateSnoozeCondition.setUsername(username);
////        System.out.println("FundingRateSnoozeCondition after mapping: " + fundingRateSnoozeCondition);
////        return fundingRateSnoozeCondition;
////    }
//
//
//}
