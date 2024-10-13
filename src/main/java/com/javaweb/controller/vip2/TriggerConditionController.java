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
                    fundingRateTriggerService.createTrigger(fundingDTO);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid trigger type");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(500).body("Error processing trigger: " + e.getMessage());
        }
            return ResponseEntity.ok("Trigger condition created successfully.");
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
