package com.javaweb.controller;

import com.javaweb.service.impl.BinanceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class APIController {

    @Autowired
    private BinanceServiceImpl binanceService;

    // Endpoint nhận yêu cầu và trả về dữ liệu thị trường dưới dạng JSON
    @GetMapping("/market-data")
    public ResponseEntity<List<Map<String, Object>>> getMarketData(@RequestParam List<String> symbols) {
        if (symbols == null || symbols.isEmpty()) {
            return ResponseEntity.badRequest().build();  // Trả về lỗi 400 nếu danh sách symbols rỗng
        }

        // Gọi service để lấy dữ liệu
        List<Map<String, Object>> marketData = binanceService.GetMarketData(symbols);

        // Trả về dữ liệu thị trường dưới dạng JSON
        return ResponseEntity.ok(marketData);
    }
}
