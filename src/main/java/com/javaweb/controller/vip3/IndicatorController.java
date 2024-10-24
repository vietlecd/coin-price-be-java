package com.javaweb.controller.vip3;

import com.javaweb.dto.IndicatorDTO;
import com.javaweb.model.UserIndicatorRequest;
import com.javaweb.service.IIndicatorService;
import com.javaweb.service.IUserIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vip3")
public class IndicatorController {
    @Autowired
    private IIndicatorService indicatorService;
    @Autowired
    private IUserIndicatorService userIndicatorService;
    @GetMapping("/indicators")
    public ResponseEntity<Map<String, IndicatorDTO>> getIndicators(
            @RequestParam("symbols") List<String> symbols,
            @RequestParam("indicators") List<String> indicators,
            @RequestParam(value = "days", required = false, defaultValue = "1") int days) {
        if (symbols == null || symbols.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Map<String, IndicatorDTO> indicatorDataMap = indicatorService.getIndicatorData(symbols, indicators, days);

        return ResponseEntity.ok(indicatorDataMap);
    }

    @PostMapping("/user-indicators")
    public ResponseEntity<String> addUserIndicator(@RequestBody UserIndicatorRequest request) {
        userIndicatorService.addIndicator(request.getName(), request.getCode());
        return ResponseEntity.ok("Đã thêm indicator " + request.getName() + " thành công!");
    }
}
