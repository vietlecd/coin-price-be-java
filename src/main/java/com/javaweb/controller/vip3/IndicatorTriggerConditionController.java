//package com.javaweb.controller.vip3;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.javaweb.dto.trigger.*;
//import com.javaweb.helpers.controller.GetUsernameHelper;
//import com.javaweb.service.trigger.*;
//import com.javaweb.service.trigger.CRUD.FundingRateTriggerService;
//import com.javaweb.service.trigger.CRUD.FuturePriceTriggerService;
//import com.javaweb.service.trigger.CRUD.IndicatorTriggerService;
//import com.javaweb.service.trigger.CRUD.PriceDifferenceTriggerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/vip3")
//public class IndicatorTriggerConditionController {
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private IndicatorTriggerService indicatorTriggerService;
//    @Autowired
//    private GetTriggerService getTriggerService;
//    @Autowired
//    private GetUsernameHelper getUsernameHelper;
//
//    @PostMapping("/create")
//    public ResponseEntity<?> createTriggerCondition(@RequestBody Map<String, Object> dtoMap,  HttpServletRequest request) {
//        try {
//            String username = getUsernameHelper.getUsername(request); // sử dụng helper cho gọn
//
//            String alertId = "";
//
//            IndicatorTriggerDTO indicatorDTO = objectMapper.convertValue(dtoMap, IndicatorTriggerDTO.class);
//            alertId = indicatorTriggerService.createTrigger(indicatorDTO, username);
//
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "Indicator created successfully");
//            response.put("alert_id", alertId);
//
//            return ResponseEntity.status(201).body(response);
//        } catch (Exception e) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("message", "Error processing trigger");
//            errorResponse.put("error", e.getMessage());
//
//            return ResponseEntity.status(500).body(errorResponse);
//
//        }
//    }
//
//    @GetMapping("/get/alerts")
//    public ResponseEntity<List<Object>> getAllTriggersByUsername(HttpServletRequest request) {
//        String username = getUsernameHelper.getUsername(request);
//        List<Object> allTriggers = getTriggerService.findAllTriggersByUsername(username);
//        return ResponseEntity.ok(allTriggers);
//    }
//
//
//    @DeleteMapping("/delete/{symbol}")
//    public ResponseEntity<?> deleteTriggerCondition(@PathVariable String symbol, HttpServletRequest request) {
//        try {
//            String username = getUsernameHelper.getUsername(request);
//
//            indicatorTriggerService.deleteTrigger(symbol, username);
//
//            return ResponseEntity.ok(symbol + " has been deleted successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error deleting trigger: " + e.getMessage());
//        }
//    }
//}
//
