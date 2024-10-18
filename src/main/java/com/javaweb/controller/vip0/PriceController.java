package com.javaweb.controller.vip0;

import com.javaweb.connect.impl.FundingIntervalWebService;
import com.javaweb.connect.impl.FundingRateWebSocketService;
import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.helpers.controller.GetUsernameHelper;
import com.javaweb.helpers.sse.SseHelper;
import com.javaweb.helpers.controller.UpperCaseHelper;
import com.javaweb.helpers.trigger.TriggerCheckHelper;
import com.javaweb.service.impl.FundingRateDataService;
import com.javaweb.service.impl.FuturePriceDataService;
import com.javaweb.service.impl.MarketCapService;
import com.javaweb.service.impl.SpotPriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/vip0")
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
    @Autowired
    private GetUsernameHelper getUsernameHelper;
    @GetMapping("/get-spot-price")
    public SseEmitter streamSpotPrices(@RequestParam List<String> symbols) {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        spotWebSocketService.connectToWebSocket(symbols);

        Map<String, PriceDTO> priceDataMap = spotPriceDataService.getPriceDataMap();

        for (String symbol : symbols) {
            sseHelper.createPriceSseEmitter(emitter, "Spot", symbol, priceDataMap, webSocketConfig);
        }
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
