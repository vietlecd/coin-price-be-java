package com.javaweb.controller.vip2;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private FundingRateTriggerService fundingRateTriggerService;

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
                    fundingRateTriggerService.createTrigger(fundingDTO);
                    break;
                case "new-symbol-listing":
                    ListingDTO listingDTO = objectMapper.convertValue(dtoMap, ListingDTO.class);
                    listingWebSocketService.startWebSocketClient();  // Khởi động WebSocket khi có trigger cho niêm yết mới
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid trigger type");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing trigger: " + e.getMessage());
        }
        return ResponseEntity.ok("Trigger condition created successfully.");
    }

    @DeleteMapping("/delete/{symbol}")
    public ResponseEntity<?> deleteTriggerCondition(@PathVariable String symbol, @RequestParam("triggerType") String triggerType) {
        try {
            if ("new-symbol-listing".equals(triggerType)) {
                listingWebSocketService.stopTokenCheck();  // Ngừng WebSocket khi xóa trigger
            }
            // Các trigger khác cũng sẽ được xử lý ở đây
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting trigger: " + e.getMessage());
        }
        return ResponseEntity.ok("Trigger condition deleted successfully.");
    }
}
