package com.javaweb.controller.vip1;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.impl.KlineWebSocketService;
import com.javaweb.dto.KlineDTO;
import com.javaweb.service.impl.KlineDataService;
import com.javaweb.service.stream.PriceStreamService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vip1")
public class KlineController {
    private PriceStreamService priceStreamService;
    private KlineWebSocketService klineWebSocketService;
    private KlineDataService klineDataService;

    @GetMapping("/get-kline")
    public SseEmitter KlinePrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        klineWebSocketService.connectToWebSocket(symbols);

        Map<String, KlineDTO> klineDataMap = klineDataService.getPriceDataMap();

        for (String symbol : symbols) {
            priceStreamService.createKlineSseEmitter(emitter, "Kline", symbol, klineDataMap);
        }
        return emitter;
    }

}
