package com.javaweb.controller.vip2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.dto.trigger.FundingRateTriggerDTO;
import com.javaweb.dto.trigger.FuturePriceTriggerDTO;
import com.javaweb.dto.trigger.PriceDifferenceTriggerDTO;
import com.javaweb.dto.trigger.SpotPriceTriggerDTO;
import com.javaweb.service.trigger.FundingRateTriggerService;
import com.javaweb.service.trigger.FuturePriceTriggerService;
import com.javaweb.service.trigger.PriceDifferenceTriggerService;
import com.javaweb.service.trigger.SpotPriceTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
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
    private FundingRateTriggerService fundingRateTriggerService;
    @PostMapping("/create")
    public ResponseEntity<?> createTriggerCondition(@RequestParam("triggerType") String triggerType, @RequestBody Map<String, Object> dtoMap) {
        try {
            String alertId = "";
            switch (triggerType) {
                case "spot":
                    SpotPriceTriggerDTO spotDTO = objectMapper.convertValue(dtoMap, SpotPriceTriggerDTO.class);
                    alertId = spotPriceTriggerService.createTrigger(spotDTO);
                    break;
                case "future":
                    FuturePriceTriggerDTO futureDTO = objectMapper.convertValue(dtoMap, FuturePriceTriggerDTO.class);
                    alertId = futurePriceTriggerService.createTrigger(futureDTO);
                    break;
                case "price-difference":
                    PriceDifferenceTriggerDTO priceDTO = objectMapper.convertValue(dtoMap, PriceDifferenceTriggerDTO.class);
                    alertId = priceDifferenceTriggerService.createTrigger(priceDTO);
                    break;
                case "funding-rate":
                    FundingRateTriggerDTO fundingDTO = objectMapper.convertValue(dtoMap, FundingRateTriggerDTO.class);
                    alertId = fundingRateTriggerService.createTrigger(fundingDTO);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid trigger type");
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Alert created successfully");
            response.put("alert_id", alertId);

            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error processing trigger");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(500).body(errorResponse);

        }
    }


//    @GetMapping("/get/{symbol}")
//    public ResponseEntity<?> getTriggerCondition(@PathVariable String symbol) {
//        TriggerConditionDTO triggerConditionDTO = triggerDataService.getTriggerConditionBySymbol(symbol);
//        return ResponseEntity.ok(triggerConditionDTO);
//    }
//
//    @PutMapping("/update/{symbol}")
//    public ResponseEntity<?> updateTriggerCondition(@PathVariable String symbol,
//                                                    @RequestBody TriggerConditionDTO triggerConditionDTO) {
//        triggerDataService.updateTriggerCondition(symbol, triggerConditionDTO);
//        return ResponseEntity.ok("Trigger condition updated successfully.");
//    }
//
//    @DeleteMapping("/delete/{symbol}")
//    public ResponseEntity<?> deleteTriggerCondition(@PathVariable String symbol) {
//        triggerDataService.deleteTriggerCondition(symbol);
//        return ResponseEntity.ok(symbol + " have been deleted successfully.");
//    }
}
