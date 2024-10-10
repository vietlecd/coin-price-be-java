package com.javaweb.controller.vip2;

import com.javaweb.dto.TriggerConditionDTO;
import com.javaweb.service.impl.TriggerDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vip2")
public class TriggerConditionController {
    @Autowired
    private TriggerDataService triggerDataService;
    @PostMapping("/create")
    public ResponseEntity<?> createTriggerCondition(@RequestBody TriggerConditionDTO triggerConditionDTO) {
        System.out.println("Received thresholdValue: " + triggerConditionDTO.getThresholdValue());
        triggerDataService.saveTriggerCondition(triggerConditionDTO);
        return ResponseEntity.ok("Trigger condition created successfully.");
    }

    @GetMapping("/get/{symbol}")
    public ResponseEntity<?> getTriggerCondition(@PathVariable String symbol) {
        TriggerConditionDTO triggerConditionDTO = triggerDataService.getTriggerConditionBySymbol(symbol);
        return ResponseEntity.ok(triggerConditionDTO);
    }

    @PutMapping("/update/{symbol}")
    public ResponseEntity<?> updateTriggerCondition(@PathVariable String symbol,
                                                    @RequestBody TriggerConditionDTO triggerConditionDTO) {
        triggerDataService.updateTriggerCondition(symbol, triggerConditionDTO);
        return ResponseEntity.ok("Trigger condition updated successfully.");
    }

    @DeleteMapping("/delete/{symbol}")
    public ResponseEntity<?> deleteTriggerCondition(@PathVariable String symbol) {
        triggerDataService.deleteTriggerCondition(symbol);
        return ResponseEntity.ok(symbol + " have been deleted successfully.");
    }
}
