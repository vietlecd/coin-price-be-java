package com.javaweb.controller.vip3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.dto.trigger.*;
import com.javaweb.service.trigger.*;
import com.javaweb.service.trigger.CRUD.FundingRateTriggerService;
import com.javaweb.service.trigger.CRUD.FuturePriceTriggerService;
import com.javaweb.service.trigger.CRUD.PriceDifferenceTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vip3")
public class IndicatorTriggerConditionController {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IndicatorTriggerService indicatorTriggerService;
    @Autowired
    private FuturePriceTriggerService futurePriceTriggerService;
    @Autowired
    private GetTriggerService getTriggerService;

    @Autowired
    private PriceDifferenceTriggerService priceDifferenceTriggerService;

    @Autowired
    private FundingRateTriggerService fundingRateTriggerService;
    @PostMapping("/create")
    public ResponseEntity<?> createTriggerCondition(@RequestParam("indicator") String indicator, @RequestBody Map<String, Object> dtoMap) {
        try {
            String alertId = "";
            switch (indicator) {
                case "MA":
                    IndicatorTriggerDTO indicatorDTO = objectMapper.convertValue(dtoMap, IndicatorTriggerDTO.class);
                    alertId = indicatorTriggerService.createTrigger(indicatorDTO);
                    break;
//                case "future":
//                    FuturePriceTriggerDTO futureDTO = objectMapper.convertValue(dtoMap, FuturePriceTriggerDTO.class);
//                    alertId = futurePriceTriggerService.createTrigger(futureDTO);
//                    break;
//                case "price-difference":
//                    PriceDifferenceTriggerDTO priceDTO = objectMapper.convertValue(dtoMap, PriceDifferenceTriggerDTO.class);
//                    alertId = priceDifferenceTriggerService.createTrigger(priceDTO);
//                    break;
//                case "funding-rate":
//                    FundingRateTriggerDTO fundingDTO = objectMapper.convertValue(dtoMap, FundingRateTriggerDTO.class);
//                    alertId = fundingRateTriggerService.createTrigger(fundingDTO);
//                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid indicator type");
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

    @GetMapping("/get/alerts")
    public ResponseEntity<List<Object>> getAllTriggersByUsername(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        List<Object> allTriggers = getTriggerService.findAllTriggersByUsername(username);
        return ResponseEntity.ok(allTriggers);
    }


    @DeleteMapping("/delete/{symbol}")
    public ResponseEntity<?> deleteTriggerCondition(@PathVariable String symbol, @RequestParam("indicator") String indicator, HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");

            switch (indicator) {
                case "MA":
                    indicatorTriggerService.deleteTrigger(symbol, username);
                    break;
//                case "future":
//                    futurePriceTriggerService.deleteTrigger(symbol, username);
//                    break;
//                case "price-difference":
//                    priceDifferenceTriggerService.deleteTrigger(symbol, username);
//                    break;
//                case "funding-rate":
//                    fundingRateTriggerService.deleteTrigger(symbol, username);
//                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid indicator type");
            }

            return ResponseEntity.ok(symbol + " has been deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting trigger: " + e.getMessage());
        }
    }
}

