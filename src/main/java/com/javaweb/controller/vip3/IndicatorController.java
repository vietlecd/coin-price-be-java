package com.javaweb.controller.vip3;

import com.javaweb.service.IIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/vip3")
public class IndicatorController {
    @Autowired
    private IIndicatorService indicatorService;
    @GetMapping("/indicators")
    public ResponseEntity<Object> getIndicators(
            @RequestParam("symbols") List<String> symbols,
            @RequestParam("indicators") List<String> indicators,
            @RequestParam(value = "days", required = false, defaultValue = "1") int days,
            HttpServletRequest request) {
        if (symbols == null || symbols.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String username = (String) request.getAttribute("username");
        try {
            Object indicatorDataMap = indicatorService.getIndicatorData(symbols, indicators, days, username);
            return ResponseEntity.ok(indicatorDataMap);
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(e.getMessage());
        } catch (IllegalArgumentException  e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
