package com.javaweb.controller.vip2;

import com.javaweb.dto.snooze.FundingRateSnoozeCondition;
import com.javaweb.dto.snooze.FutureSnoozeCondition;
import com.javaweb.dto.snooze.PriceDifferenceSnoozeCondition;
import com.javaweb.dto.snooze.SpotSnoozeCondition;
import com.javaweb.helpers.controller.GetUsernameHelper;
import com.javaweb.helpers.trigger.SnoozeMapHelper;
import com.javaweb.service.snooze.FundingRateSnoozeConditionService;
import com.javaweb.service.snooze.FutureSnoozeConditionService;
import com.javaweb.service.snooze.PriceDifferenceSnoozeConditionService;
import com.javaweb.service.snooze.SpotSnoozeConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/vip2")
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
    @Autowired
    private GetUsernameHelper getUsernameHelper;

    @PostMapping("/create/snooze")
    public ResponseEntity<?> createSnoozeCondition(@RequestParam("snoozeType") String snoozeType,
                                                   @RequestBody Map<String, Object> snoozeConditionRequest, HttpServletRequest request) {

        try {
            String username = (String) request.getAttribute("username");
            switch (snoozeType) {
                case "spot":
                    SpotSnoozeCondition spotSnoozeCondition = snoozeMapHelper.mapToSpotSnoozeCondition(snoozeConditionRequest, username);
                    spotSnoozeConditionService.createSnoozeCondition(spotSnoozeCondition, username);
                    break;
                case "future":
                    FutureSnoozeCondition futureSnoozeCondition = snoozeMapHelper.mapToFutureSnoozeCondition(snoozeConditionRequest, username);
                    futureSnoozeConditionService.createFutureSnoozeCondition(futureSnoozeCondition, username);
                    break;
                case "price-difference":
                    PriceDifferenceSnoozeCondition priceDifferenceSnoozeCondition = snoozeMapHelper.mapToPriceDifferenceSnoozeCondition(snoozeConditionRequest, username);
                    priceDifferenceSnoozeConditionService.createSnoozeCondition(priceDifferenceSnoozeCondition, username);
                    break;
                case "funding-rate":
                    FundingRateSnoozeCondition fundingRateSnoozeCondition = snoozeMapHelper.mapToFundingRateSnoozeCondition(snoozeConditionRequest, username);
                    fundingRateSnoozeConditionService.createSnoozeCondition(fundingRateSnoozeCondition, username);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid trigger type");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing trigger: " + e.getMessage());
        }
        return ResponseEntity.ok("Snooze condition created successfully.");
    }

    @DeleteMapping("/delete/snooze/{symbol}")
    public ResponseEntity<?> deleteSnoozeCondition(@PathVariable String symbol, @RequestParam("snoozeType") String snoozeType, HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");

            switch (snoozeType) {
                case "spot":
                    spotSnoozeConditionService.deleteSnoozeCondition(symbol, username);
                    break;
                case "future":
                    futureSnoozeConditionService.deleteSnoozeCondition(symbol, username);
                    break;
                case "price-difference":
                    priceDifferenceSnoozeConditionService.deleteSnoozeCondition(symbol, username);
                    break;
                case "funding-rate":
                    fundingRateSnoozeConditionService.deleteSnoozeCondition(symbol, username);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid snooze condition type");
            }

            return ResponseEntity.ok("Snooze condition for symbol " + symbol + " has been deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting snooze condition: " + e.getMessage());
        }
    }
}

