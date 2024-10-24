package com.javaweb.controller.vip2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.trigger.FundingRateTriggerDTO;
import com.javaweb.dto.trigger.FuturePriceTriggerDTO;
import com.javaweb.dto.trigger.ListingDTO;
import com.javaweb.dto.trigger.PriceDifferenceTriggerDTO;
import com.javaweb.dto.trigger.SpotPriceTriggerDTO;
import com.javaweb.service.trigger.*;
import com.javaweb.connect.impl.ListingWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/vip2")
public class TriggerConditionController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpotPriceTriggerService spotPriceTriggerService;

    @Autowired
    private FuturePriceTriggerService futurePriceTriggerService;

    @Autowired
    private PriceDifferenceTriggerService priceDifferenceTriggerService;

    @Autowired
    private FundingRateTriggerService fundingRateTriggerService; // Assuming this service exists

    @Autowired
    private FundingRateIntervalService fundingRateIntervalService;

    @Autowired
    private ListingWebSocketService listingWebSocketService;

    @PostMapping("/create")
    public ResponseEntity<?> createTriggerCondition(@RequestParam("triggerType") String triggerType, @RequestBody Map<String, Object> dtoMap) {
        try {
            switch (triggerType) {
                case "spot":
                    SpotPriceTriggerDTO spotDTO = objectMapper.convertValue(dtoMap, SpotPriceTriggerDTO.class);
                    spotPriceTriggerService.createTrigger(spotDTO);
                    break;
                case "future":
                    FuturePriceTriggerDTO futureDTO = objectMapper.convertValue(dtoMap, FuturePriceTriggerDTO.class);
                    futurePriceTriggerService.createTrigger(futureDTO);
                    break;
                case "price-difference":
                    PriceDifferenceTriggerDTO priceDTO = objectMapper.convertValue(dtoMap, PriceDifferenceTriggerDTO.class);
                    priceDifferenceTriggerService.createTrigger(priceDTO);
                    break;
                case "funding-rate":
                    FundingRateTriggerDTO fundingDTO = objectMapper.convertValue(dtoMap, FundingRateTriggerDTO.class);
                    fundingRateTriggerService.createTrigger(fundingDTO); // Ensure this method exists in the FundingRateTriggerService
                    break;
                case "new-symbol-listing":
                    ListingDTO listingDTO = objectMapper.convertValue(dtoMap, ListingDTO.class);
                    listingWebSocketService.startWebSocketClient();  // Start WebSocket for new listing trigger
                    break;
                case "funding-rate-interval-changed":
                    FundingIntervalDTO fundingIntervalDTO = objectMapper.convertValue(dtoMap, FundingIntervalDTO.class);
                    fundingRateIntervalService.createFundingIntervalTrigger(fundingIntervalDTO); // Call the correct method
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid trigger type");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing trigger: " + e.getMessage());
        }
        return ResponseEntity.ok("Trigger condition created successfully."); // Ensure return here
    }

    @DeleteMapping("/delete/{symbol}")
    public ResponseEntity<?> deleteTriggerCondition(@PathVariable String symbol, @RequestParam("triggerType") String triggerType) {
        try {
            switch (triggerType) {
                case "funding-rate-interval":
                    fundingRateIntervalService.deleteFundingRateInterval(symbol); // Delete logic for funding rate interval
                    break;
                case "new-symbol-listing":
                    listingWebSocketService.stopTokenCheck();  // Stop WebSocket when deleting trigger
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid trigger type");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting trigger: " + e.getMessage());
        }
        return ResponseEntity.ok("Trigger condition deleted successfully.");
    }
}
