package com.javaweb.controller.vip2;

import com.javaweb.dto.snooze.FundingRateSnoozeCondition;
import com.javaweb.dto.snooze.FutureSnoozeCondition;
import com.javaweb.dto.snooze.PriceDifferenceSnoozeCondition;
import com.javaweb.dto.snooze.SpotSnoozeCondition;
import com.javaweb.helpers.trigger.SnoozeMapHelper;
import com.javaweb.service.snooze.FundingRateSnoozeConditionService;
import com.javaweb.service.snooze.FutureSnoozeConditionService;
import com.javaweb.service.snooze.PriceDifferenceSnoozeConditionService;
import com.javaweb.service.snooze.SpotSnoozeConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/vip2")
public class SnoozeConditionController {

    @Autowired
    private SpotSnoozeConditionService spotSnoozeConditionService;

    @Autowired
    private FutureSnoozeConditionService futureSnoozeConditionService;

    @Autowired
    private PriceDifferenceSnoozeConditionService priceDifferenceSnoozeConditionService;

    @Autowired
    private FundingRateSnoozeConditionService fundingRateSnoozeConditionService;

    @Autowired
    private SnoozeMapHelper snoozeMapHelper;

    @PostMapping("/create/snooze")
    public ResponseEntity<?> createTriggerCondition(@RequestParam("triggerType") String triggerType,
                                                    @RequestBody Map<String, Object> snoozeConditionRequest) {
        try {
            switch (triggerType) {
                case "spot":
                    SpotSnoozeCondition spotSnoozeCondition = snoozeMapHelper.mapToSpotSnoozeCondition(snoozeConditionRequest);
                    spotSnoozeConditionService.createSnoozeCondition(spotSnoozeCondition);
                    break;
                case "future":
                    FutureSnoozeCondition futureSnoozeCondition = snoozeMapHelper.mapToFutureSnoozeCondition(snoozeConditionRequest);
                    futureSnoozeConditionService.createSnoozeCondition(futureSnoozeCondition);
                    break;
                case "price-difference":
                    PriceDifferenceSnoozeCondition priceDifferenceSnoozeCondition = snoozeMapHelper.mapToPriceDifferenceSnoozeCondition(snoozeConditionRequest);
                    priceDifferenceSnoozeConditionService.createSnoozeCondition(priceDifferenceSnoozeCondition);
                    break;
                case "funding-rate":
                    FundingRateSnoozeCondition fundingRateSnoozeCondition = snoozeMapHelper.mapToFundingRateSnoozeCondition(snoozeConditionRequest);
                    fundingRateSnoozeConditionService.createSnoozeCondition(fundingRateSnoozeCondition);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid trigger type");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing trigger: " + e.getMessage());
        }
        return ResponseEntity.ok("Snooze condition created successfully.");
    }
}
