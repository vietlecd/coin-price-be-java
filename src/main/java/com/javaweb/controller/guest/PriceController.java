package com.javaweb.controller.guest;

import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.dto.PriceDTO;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.helpers.controller.GetUsernameHelper;
import com.javaweb.helpers.sse.SseHelper;
import com.javaweb.model.MarketCapResponse;
import com.javaweb.service.impl.FuturePriceDataService;
import com.javaweb.service.impl.MarketCapService;
import com.javaweb.service.impl.SpotPriceDataService;
import com.javaweb.service.trigger.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api")
public class PriceController {
    private final ConcurrentHashMap<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Autowired
    private SseHelper sseHelper;
    @Autowired
    private GetUsernameHelper getUsernameHelper;
    @Autowired
    private TriggerService triggerService;
    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private SpotPriceDataService spotPriceDataService;
    @Autowired
    private FuturePriceDataService futurePriceDataService;
    @Autowired
    private MarketCapService marketCapService;

    @Autowired
    private SpotWebSocketService spotWebSocketService;
    @Autowired
    private FutureWebSocketService futureWebSocketService;

    @GetMapping("/get-spot-price")
    public SseEmitter streamSpotPrices(@RequestParam List<String> symbols, HttpServletRequest request) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        spotWebSocketService.connectToWebSocket(symbols);

        Map<String, PriceDTO> priceDataMap = spotPriceDataService.getPriceDataMap();

        for (String symbol : symbols) {
            sseHelper.createPriceSseEmitter(emitter, "Spot", symbol, priceDataMap, webSocketConfig);
        }

        return emitter;
    }

    @GetMapping("/get-future-price")
    public SseEmitter streamFuturePrices(@RequestParam List<String> symbols, HttpServletRequest request) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        futureWebSocketService.connectToWebSocket(symbols);

        Map<String, PriceDTO> priceDataMap = futurePriceDataService.getPriceDataMap();
        for (String symbol : symbols) {
            sseHelper.createPriceSseEmitter(emitter, "Future", symbol, priceDataMap, webSocketConfig);
        }

        return emitter;
    }

    @GetMapping("/get-funding-rate")
    public SseEmitter streamFundingRate(@RequestParam List<String> symbols, HttpServletRequest request) {
        String username = getUsernameHelper.getUsername(request);
        return triggerService.handleStreamFundingRate(symbols, username, webSocketConfig);
    }

    @GetMapping("/get-market")
    // Phương thức mới nhận 'symbol' từ query parameter
    public List<MarketCapResponse> getMarketCap(@RequestParam List<String> symbols) {
        List<MarketCapResponse> marketCapResponses = new ArrayList<>();

        // Lặp qua từng symbol trong danh sách và lấy kết quả từ service
        for (String symbol : symbols) {
            MarketCapResponse response = marketCapService.getMarketCapBySymbol(symbol);
            marketCapResponses.add(response);  // Thêm kết quả vào danh sách
        }

        // Trả về danh sách các kết quả
        return marketCapResponses;
    }

    @DeleteMapping("/close-websocket")
    public void closeWebSocket(@RequestParam String type) {
        sseHelper.closeWebSocket(type, webSocketConfig);
    }

    @DeleteMapping("/close-all-websocket")
    public void closeAllWebSocket() {
        sseHelper.closeAllWebSockets(webSocketConfig);
    }

}


