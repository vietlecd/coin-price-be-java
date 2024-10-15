package com.javaweb.controller.vip2;


import com.javaweb.dto.snooze.SnoozeCondition;
import com.javaweb.service.IPriceDataService;
import com.javaweb.service.impl.SpotPriceDataService;
import com.javaweb.service.snooze.SnoozeConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/snooze")
public class SnoozeConditionController {

    @Autowired
    private SnoozeConditionService snoozeConditionService;



    @PostMapping("/create")
    public SnoozeCondition createSnoozeCondition(@RequestBody SnoozeCondition snoozeCondition) {
        return snoozeConditionService.createSnoozeCondition(snoozeCondition);
    }


    @GetMapping("/check/{triggerId}")
    public boolean checkSnooze(@PathVariable String triggerId) {

        return snoozeConditionService.isSnoozeActive(triggerId);
    }

    // Endpoint to deactivate a snooze condition
    @DeleteMapping("/delete/{triggerId}")
    public ResponseEntity<Void> deleteSnoozeConditionByTriggerId(@PathVariable String triggerId) {
        snoozeConditionService.deleteSnoozeConditionByTriggerId(triggerId);
        return ResponseEntity.noContent().build();
    }
}
