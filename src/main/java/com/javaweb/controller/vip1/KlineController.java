package com.javaweb.controller.vip1;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.impl.KlineWebSocketService;
import com.javaweb.dto.KlineDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.sse.SseHelper;
import com.javaweb.service.impl.KlineDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/vip1")
public class KlineController {
    @Autowired
    private SseHelper sseHelper;

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private KlineWebSocketService klineWebSocketService;

    @Autowired
    private KlineDataService klineDataService;

    @GetMapping("/api/get-kline")
    public SseEmitter KlinePrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        klineWebSocketService.connectToWebSocket(symbols);

        Map<String, KlineDTO> klineDataMap = klineDataService.getPriceDataMap();

        for (String symbol : symbols) {
            sseHelper.createKlineSseEmitter(emitter, "Kline", symbol, klineDataMap, webSocketConfig);
        }
        return emitter;
    }

}
