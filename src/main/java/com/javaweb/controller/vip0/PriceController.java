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
import com.javaweb.service.impl.FundingRateDataService;
import com.javaweb.service.impl.FuturePriceDataService;
import com.javaweb.service.impl.MarketCapService;
import com.javaweb.service.impl.SpotPriceDataService;
import com.javaweb.service.trigger.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        String username = getUsernameHelper.getUsername(request);

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        spotWebSocketService.connectToWebSocket(symbols);

        Map<String, PriceDTO> priceDataMap = spotPriceDataService.getPriceDataMap();

        if (username == null || username.isEmpty()){
            for (String symbol : symbols) {
                sseHelper.createPriceSseEmitter(emitter, "Spot", symbol, priceDataMap, webSocketConfig);
            }
        }
        else {
            return triggerService.handleStreamPrice("Spot", symbols, username, priceDataMap, webSocketConfig);
        }

        return emitter;
    }

    @GetMapping("/get-future-price")
    public SseEmitter streamFuturePrices(@RequestParam List<String> symbols,  HttpServletRequest request) {
        String username = getUsernameHelper.getUsername(request);
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        futureWebSocketService.connectToWebSocket(symbols);

        Map<String, PriceDTO> priceDataMap = futurePriceDataService.getPriceDataMap();
        if (username == null || username.isEmpty()) {
            for (String symbol : symbols) {
                sseHelper.createPriceSseEmitter(emitter, "Future", symbol, priceDataMap, webSocketConfig);
            }
        } else {
            return triggerService.handleStreamPrice("Future", symbols, username, priceDataMap, webSocketConfig);
        }

        return emitter;
    }

//    @GetMapping("compare-prices")
//    public SseEmitter compareSpotAndFuturePrices(@RequestParam List<String> symbols, HttpServletRequest request) {
//        String username = getUsernameHelper.getUsername(request);
//        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
//
//        spotWebSocketService.connectToWebSocket(symbols);
//        futureWebSocketService.connectToWebSocket(symbols);
//
//        Map<String, PriceDTO> spotPriceDataMap = spotPriceDataService.getPriceDataMap();
//        Map<String, PriceDTO> futurePriceDataMap = futurePriceDataService.getPriceDataMap();
//
//        return triggerService.handleStreamComparePrice(symbols, username, spotPriceDataMap, futurePriceDataMap, webSocketConfig);
//    }

//    @GetMapping("/get-funding-rate")
//    public SseEmitter streamFundingRate(@RequestParam List<String> symbols, HttpServletRequest request) {
//        String username = getUsernameHelper.getUsername(request);
//
//        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
//        fundingRateWebSocketService.connectToWebSocket(symbols);
//
//        Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataMap();
//        List<Map<String, FundingIntervalDTO>> fundingIntervalDataList = fundingIntervalWebService.getLatestFundingIntervalData(symbols);
//        if (username == null || username.isEmpty()) {
//            for (String symbol : symbols) {
//                sseHelper.createFundingRateSseEmitter(emitter, "FundingRate", symbol, fundingRateDataMap, webSocketConfig);
//
//                // SSE cho FundingInterval
//                for (Map<String, FundingIntervalDTO> fundingIntervalData : fundingIntervalDataList) {
//                    if (fundingIntervalData.containsKey(symbol)) {
//                        FundingIntervalDTO intervalDTO = fundingIntervalData.get(symbol);
//                        try {
//                            emitter.send(SseEmitter.event().name("FundingInterval").data(intervalDTO));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        } else {
//            return triggerService.handleStreamFundingRate("FundingRate", symbols, username, fundingRateDataMap, webSocketConfig);
//        }
//
//        return emitter;
//    }

    @GetMapping("/get-funding-rate")
    public SseEmitter streamFundingRate(@RequestParam List<String> symbols, HttpServletRequest request) {
        String username = getUsernameHelper.getUsername(request);

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        fundingRateWebSocketService.connectToWebSocket(symbols);
        Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataMap();

        for (String symbol : symbols) {
            sseHelper.createFundingRateSseEmitter(emitter, "FundingRate", symbol, fundingRateDataMap, webSocketConfig);
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            List<Map<String, FundingIntervalDTO>> fundingIntervalDataList = fundingIntervalWebService.getLatestFundingIntervalData(symbols);

            for (String symbol : symbols) {
                for (Map<String, FundingIntervalDTO> fundingIntervalData : fundingIntervalDataList) {
                    if (fundingIntervalData.containsKey(symbol)) {
                        FundingIntervalDTO intervalDTO = fundingIntervalData.get(symbol);
                        try {
                            emitter.send(SseEmitter.event().name("FundingInterval").data(intervalDTO));
                        } catch (Exception e) {
                            e.printStackTrace();
                            emitter.completeWithError(e);
                        }
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

        ScheduledExecutorService dataUpdater = Executors.newScheduledThreadPool(1);
        dataUpdater.scheduleAtFixedRate(() -> {
            fundingIntervalWebService.getLatestFundingIntervalData(symbols);
            System.out.println("FundingInterval data updated for symbols: " + symbols);
        }, 0, 15, TimeUnit.MINUTES);

        return emitter;
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
