package com.javaweb.controller.vip2;

import com.javaweb.dto.trigger.FundingRateTriggerDTO;
import com.javaweb.dto.trigger.PriceDifferenceTriggerDTO;
import com.javaweb.dto.trigger.SpotFuturePriceTriggerDTO;
import com.javaweb.service.trigger.FundingRateTriggerService;
import com.javaweb.service.trigger.PriceDifferenceTriggerService;
import com.javaweb.service.trigger.SpotFuturePriceTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vip2")
public class TriggerConditionController {
    @Autowired
    private SpotFuturePriceTriggerService spotFuturePriceTriggerService;

    @Autowired
    private PriceDifferenceTriggerService priceDifferenceTriggerService;

    @Autowired
    private FundingRateTriggerService fundingRateTriggerService;
    @PostMapping("/create")
    public ResponseEntity<?> createTriggerCondition(@RequestBody Object dto) {
        if (dto instanceof SpotFuturePriceTriggerDTO) {
            spotFuturePriceTriggerService.createTrigger((SpotFuturePriceTriggerDTO) dto);
        } else if (dto instanceof PriceDifferenceTriggerDTO) {
            priceDifferenceTriggerService.createTrigger((PriceDifferenceTriggerDTO) dto);
        } else if (dto instanceof FundingRateTriggerDTO) {
            fundingRateTriggerService.createTrigger((FundingRateTriggerDTO) dto);
        } else {
            return ResponseEntity.badRequest().body("Invalid trigger type");
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
