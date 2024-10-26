package com.javaweb.controller.vip3;

import com.javaweb.dto.IndicatorDTO;
import com.javaweb.service.IIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vip3")
public class IndicatorController {
    @Autowired
    private IIndicatorService indicatorService;
    @GetMapping("/indicators")
    public ResponseEntity<Map<String, IndicatorDTO>> getIndicators(
            @RequestParam("symbols") List<String> symbols,
            @RequestParam("indicators") List<String> indicators,
            @RequestParam(value = "days", required = false, defaultValue = "1") int days,
            HttpServletRequest request) {
        if (symbols == null || symbols.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String username = (String) request.getAttribute("username");

        Map<String, IndicatorDTO> indicatorDataMap = indicatorService.getIndicatorData(symbols, indicators, days, username);

        return ResponseEntity.ok(indicatorDataMap);
    }
}
