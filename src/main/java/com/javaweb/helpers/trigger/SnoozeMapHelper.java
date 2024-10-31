package com.javaweb.helpers.trigger;

import com.javaweb.dto.snooze.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class SnoozeMapHelper {
    public SpotSnoozeCondition mapToSpotSnoozeCondition(Map<String, Object> snoozeConditionRequest, String username) {
        // Perform the mapping of the request body to SpotSnoozeCondition
        SpotSnoozeCondition spotSnoozeCondition = new SpotSnoozeCondition(

                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );

        // Set the username (usernameId) for the SpotSnoozeCondition object
        if (snoozeConditionRequest.get("repeatCount") != null) {
            spotSnoozeCondition.setRepeatCount((Integer) snoozeConditionRequest.get("repeatCount"));
        } else {
            spotSnoozeCondition.setRepeatCount(0); // default value if not provided
        }

        if (snoozeConditionRequest.get("maxRepeatCount") != null) {
            spotSnoozeCondition.setMaxRepeatCount((Integer) snoozeConditionRequest.get("maxRepeatCount"));
        } else {
            spotSnoozeCondition.setMaxRepeatCount(1); // default value if not provided
        }

        // Print out the object after mapping for debugging purposes
        System.out.println("SpotSnoozeCondition after mapping: " + spotSnoozeCondition);
        spotSnoozeCondition.setUsername(username);
        System.out.println("SpotSnoozeCondition after mapping: " + spotSnoozeCondition);
        return spotSnoozeCondition;
    }


    // Method to map the request body to a FutureSnoozeCondition DTO
    public FutureSnoozeCondition mapToFutureSnoozeCondition(Map<String, Object> snoozeConditionRequest, String username) {
        // Perform the mapping of the request body to FutureSnoozeCondition
        FutureSnoozeCondition futureSnoozeCondition = new FutureSnoozeCondition(

                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );

        // Set the username (usernameId) for the FutureSnoozeCondition object
        if (snoozeConditionRequest.get("repeatCount") != null) {
            futureSnoozeCondition.setRepeatCount((Integer) snoozeConditionRequest.get("repeatCount"));
        } else {
            futureSnoozeCondition.setRepeatCount(0); // default value if not provided
        }

        if (snoozeConditionRequest.get("maxRepeatCount") != null) {
            futureSnoozeCondition.setMaxRepeatCount((Integer) snoozeConditionRequest.get("maxRepeatCount"));
        } else {
            futureSnoozeCondition.setMaxRepeatCount(1); // default value if not provided
        }

        // Print out the object after mapping for debugging purposes
        System.out.println("FutureSnoozeCondition after mapping: " + futureSnoozeCondition);
        futureSnoozeCondition.setUsername(username);
        System.out.println("FutureSnoozeCondition after mapping: " + futureSnoozeCondition);
        return futureSnoozeCondition;
    }


    // Method to map the request body to a PriceDifferenceSnoozeCondition DTO
    public PriceDifferenceSnoozeCondition mapToPriceDifferenceSnoozeCondition(Map<String, Object> snoozeConditionRequest, String username) {
        // Perform the mapping of the request body to PriceDifferenceSnoozeCondition
        PriceDifferenceSnoozeCondition priceDifferenceSnoozeCondition = new PriceDifferenceSnoozeCondition(

                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );

        // Set the username (usernameId) for the PriceDifferenceSnoozeCondition object
        priceDifferenceSnoozeCondition.setUsername(username);
        System.out.println("PriceDifferenceSnoozeCondition after mapping: " + priceDifferenceSnoozeCondition);
        return priceDifferenceSnoozeCondition;
    }


    public FundingRateSnoozeCondition mapToFundingRateSnoozeCondition(Map<String, Object> snoozeConditionRequest, String username) {
        // Perform the mapping of the request body to FundingRateSnoozeCondition
        FundingRateSnoozeCondition fundingRateSnoozeCondition = new FundingRateSnoozeCondition(

                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );

        // Set the username (usernameId) for the FundingRateSnoozeCondition object
        fundingRateSnoozeCondition.setUsername(username);
        System.out.println("FundingRateSnoozeCondition after mapping: " + fundingRateSnoozeCondition);
        return fundingRateSnoozeCondition;
    }
    public IndicatorSnoozeCondition mapToIndicatorSnoozeCondition(Map<String, Object> snoozeConditionRequest, String username) {
        // Perform the mapping of the request body to IndicatorSnoozeCondition
        IndicatorSnoozeCondition indicatorSnoozeCondition = new IndicatorSnoozeCondition(

                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );

        // Set the username (usernameId) for the IndicatorSnoozeCondition object
        if (snoozeConditionRequest.get("repeatCount") != null) {
            indicatorSnoozeCondition.setRepeatCount((Integer) snoozeConditionRequest.get("repeatCount"));
        } else {
            indicatorSnoozeCondition.setRepeatCount(0); // default value if not provided
        }

        if (snoozeConditionRequest.get("maxRepeatCount") != null) {
            indicatorSnoozeCondition.setMaxRepeatCount((Integer) snoozeConditionRequest.get("maxRepeatCount"));
        } else {
            indicatorSnoozeCondition.setMaxRepeatCount(1); // default value if not provided
        }

        // Print out the object after mapping for debugging purposes
        System.out.println("IndicatorSnoozeCondition after mapping: " + indicatorSnoozeCondition);
        indicatorSnoozeCondition.setUsername(username);
        System.out.println("IndicatorSnoozeCondition after mapping: " + indicatorSnoozeCondition);
        return indicatorSnoozeCondition;
    }

    public IntervalSnoozeCondition mapToIntervalSnoozeCondition(Map<String, Object> snoozeConditionRequest, String username) {
        // Perform the mapping of the request body to IntervalSnoozeCondition
        IntervalSnoozeCondition intervalSnoozeCondition = new IntervalSnoozeCondition(
                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );

        // Set the username (usernameId) for the IntervalSnoozeCondition object
        if (snoozeConditionRequest.get("repeatCount") != null) {
            intervalSnoozeCondition.setRepeatCount((Integer) snoozeConditionRequest.get("repeatCount"));
        } else {
            intervalSnoozeCondition.setRepeatCount(0); // default value if not provided
        }

        if (snoozeConditionRequest.get("maxRepeatCount") != null) {
            intervalSnoozeCondition.setMaxRepeatCount((Integer) snoozeConditionRequest.get("maxRepeatCount"));
        } else {
            intervalSnoozeCondition.setMaxRepeatCount(1); // default value if not provided
        }

        // Print out the object after mapping for debugging purposes
        System.out.println("IntervalSnoozeCondition after mapping: " + intervalSnoozeCondition);
        intervalSnoozeCondition.setUsername(username);
        System.out.println("IntervalSnoozeCondition after mapping: " + intervalSnoozeCondition);
        return intervalSnoozeCondition;
    }
    public ListingSnoozeCondition mapToListingSnoozeCondition(Map<String, Object> snoozeConditionRequest, String username) {
        // Perform the mapping of the request body to ListingSnoozeCondition
        ListingSnoozeCondition listingSnoozeCondition = new ListingSnoozeCondition(

                (String) snoozeConditionRequest.get("symbol"),
                (String) snoozeConditionRequest.get("conditionType"),
                LocalDateTime.parse((String) snoozeConditionRequest.get("startTime")),
                LocalDateTime.parse((String) snoozeConditionRequest.get("endTime")),
                (String) snoozeConditionRequest.get("specificTime")
        );

        // Set the username (usernameId) for the ListingSnoozeCondition object
        if (snoozeConditionRequest.get("repeatCount") != null) {
            listingSnoozeCondition.setRepeatCount((Integer) snoozeConditionRequest.get("repeatCount"));
        } else {
            listingSnoozeCondition.setRepeatCount(0); // default value if not provided
        }

        if (snoozeConditionRequest.get("maxRepeatCount") != null) {
            listingSnoozeCondition.setMaxRepeatCount((Integer) snoozeConditionRequest.get("maxRepeatCount"));
        } else {
            listingSnoozeCondition.setMaxRepeatCount(1); // default value if not provided
        }

        // Print out the object after mapping for debugging purposes
        System.out.println("ListingSnoozeCondition after mapping: " + listingSnoozeCondition);
        listingSnoozeCondition.setUsername(username);
        System.out.println("ListingSnoozeCondition after mapping: " + listingSnoozeCondition);
        return listingSnoozeCondition;
    }


}
