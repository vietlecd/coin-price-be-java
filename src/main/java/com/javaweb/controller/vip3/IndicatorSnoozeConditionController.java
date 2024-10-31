package com.javaweb.controller.vip3;

import com.javaweb.dto.snooze.IndicatorSnoozeCondition;
import com.javaweb.helpers.controller.GetUsernameHelper;
import com.javaweb.helpers.trigger.SnoozeMapHelper;
import com.javaweb.service.snooze.IndicatorSnoozeConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/vip3")
public class IndicatorSnoozeConditionController {

    @Autowired
    private IndicatorSnoozeConditionService indicatorSnoozeConditionService;

    @Autowired
    private SnoozeMapHelper snoozeMapHelper;

    @Autowired
    private GetUsernameHelper getUsernameHelper;

    @PostMapping("/create/snooze_indicator")
    public ResponseEntity<?> createSnoozeCondition(@RequestBody Map<String, Object> snoozeConditionRequest, HttpServletRequest request) {

        try {
            String username = (String) request.getAttribute("username");

                IndicatorSnoozeCondition indicatorSnoozeCondition = snoozeMapHelper.mapToIndicatorSnoozeCondition(snoozeConditionRequest, username);
                indicatorSnoozeConditionService.createSnoozeCondition(indicatorSnoozeCondition, username);



        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing trigger: " + e.getMessage());
        }
        return ResponseEntity.ok("Snooze condition created successfully.");
    }

    @DeleteMapping("/delete/snooze/{symbol}")
    public ResponseEntity<?> deleteSnoozeCondition(@PathVariable String symbol, @RequestParam("snoozeType") String snoozeType, HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            if ("indicator".equals(snoozeType)) {
                indicatorSnoozeConditionService.deleteSnoozeCondition(symbol, username);
            } else {
                return ResponseEntity.badRequest().body("Invalid snooze condition type");
            }

            return ResponseEntity.ok("Snooze condition for symbol " + symbol + " has been deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting snooze condition: " + e.getMessage());
        }
    }
}
