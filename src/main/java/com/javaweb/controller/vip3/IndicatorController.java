//package com.javaweb.controller.vip3;
//
//import com.javaweb.dto.IndicatorDTO;
//import com.javaweb.service.impl.IndicatorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/vip3")
//public class IndicatorController {
//    @Autowired
//    private IndicatorService indicatorService;
//    @GetMapping("/indicators")
//    public ResponseEntity<Map<String, IndicatorDTO>> getIndicators(
//            @RequestParam("symbols") List<String> symbols,
//            @RequestParam("indicators") List<String> indicators,
//            @RequestParam(value = "days", required = false, defaultValue = "1") int days) {
//        if (symbols == null || symbols.isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Map<String, IndicatorDTO> indicatorDataMap = indicatorService.getIndicatorData(symbols, indicators, days);
//
//        return ResponseEntity.ok(indicatorDataMap);
//    }
//}
