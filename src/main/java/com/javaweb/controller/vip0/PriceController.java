package com.javaweb.controller.vip0;

import com.javaweb.connect.impl.FundingIntervalWebService;
import com.javaweb.connect.impl.FundingRateWebSocketService;
import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.helpers.sse.SseHelper;
import com.javaweb.helpers.controller.UpperCaseHelper;
import com.javaweb.helpers.trigger.TriggerCheckHelper;
import com.javaweb.service.impl.FundingRateDataService;
import com.javaweb.service.impl.FuturePriceDataService;
import com.javaweb.service.impl.MarketCapService;
import com.javaweb.service.impl.SpotPriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
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
    private TriggerCheckHelper triggerCheckHelper;
    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private SpotPriceDataService spotPriceDataService;
    @Autowired
    private FuturePriceDataService futurePriceDataService;
    @Autowired
    private FundingRateDataService fundingRateDataService;
    @Autowired
    private MarketCapService marketCapService;

    @Autowired
    private SpotWebSocketService spotWebSocketService;
    @Autowired
    private FutureWebSocketService futureWebSocketService;
    @Autowired
    private FundingRateWebSocketService fundingRateWebSocketService;
    @Autowired
    private FundingIntervalWebService fundingIntervalWebService;

    @GetMapping("/get-spot-price")
    public SseEmitter streamSpotPrices(@RequestParam List<String> symbols, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null || username.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        spotWebSocketService.connectToWebSocket(symbols);

        Map<String, PriceDTO> priceDataMap = spotPriceDataService.getPriceDataMap();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            // Kiểm tra các symbols nào đã được kích hoạt trigger
            List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, priceDataMap, "spot", username);

            // Nếu có symbols thỏa mãn điều kiện trigger, in ra username và symbol tương ứng
            if (!firedSymbols.isEmpty()) {
                for (String symbol : firedSymbols) {
                    System.out.println("Trigger fired for user: " + username + " and symbol: " + symbol);
                    // Gửi thông báo qua SSE khi trigger được kích hoạt
                    try {
                        emitter.send(SseEmitter.event().name("trigger").data("Trigger fired for " + username + " and symbol: " + symbol));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("No triggers fired for user: " + username + symbols);
            }
        }, 0, 5, TimeUnit.SECONDS); // Kiểm tra mỗi 5 giây

        // Tạo SSE emitter cho từng symbol
        for (String symbol : symbols) {
            sseHelper.createPriceSseEmitter(emitter, "Spot", symbol, priceDataMap, webSocketConfig);
        }


        emitter.onCompletion(() -> scheduler.shutdown());



//        for (String symbol : symbols) {
//            sseHelper.createPriceSseEmitter(emitter, "Spot", symbol, priceDataMap, webSocketConfig);
//        }
        return emitter;
    }

    @GetMapping("/get-future-price")
    public SseEmitter streamFuturePrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        futureWebSocketService.connectToWebSocket(symbols);

        Map<String, PriceDTO> priceDataMap = futurePriceDataService.getPriceDataMap();
        for (String symbol : symbols) {
            sseHelper.createPriceSseEmitter(emitter, "Future", symbol, priceDataMap, webSocketConfig);
        }
        return emitter;
    }

    @GetMapping("/get-funding-rate")
    public SseEmitter streamFundingRate(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        fundingRateWebSocketService.connectToWebSocket(symbols);

        Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataMap();
        for (String symbol : symbols) {
            sseHelper.createFundingRateSseEmitter(emitter, "FundingRate", symbol, fundingRateDataMap, webSocketConfig);
        }
        return emitter;
    }

    @GetMapping("/get-funding-interval")
    public ResponseEntity<List<Map<String, FundingIntervalDTO>>> getFundingInterval(@RequestParam List<String> symbols) {
        List<String> upperCasesymbols = UpperCaseHelper.converttoUpperCase(symbols);

        List<Map<String, FundingIntervalDTO>> fundingIntervalData = fundingIntervalWebService.handleFundingIntervalWeb(upperCasesymbols);
        return ResponseEntity.ok(fundingIntervalData);
    }

    @GetMapping("/get-market")
    public ResponseEntity<List<Map<String, Object>>> getMarketData(@RequestParam List<String> symbols) {
        if (symbols == null || symbols.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Map<String, Object>> marketData = marketCapService.getMarketData(symbols);

        return ResponseEntity.ok(marketData);
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
