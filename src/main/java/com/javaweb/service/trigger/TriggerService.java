package com.javaweb.service.trigger;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.impl.FundingIntervalWebService;
import com.javaweb.connect.impl.FundingRateWebSocketService;
import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.controller.FundingRateAndIntervalHelper;
import com.javaweb.helpers.sse.SseHelper;
import com.javaweb.helpers.trigger.TriggerCheckHelper;
import com.javaweb.service.impl.FundingRateDataService;
import com.javaweb.service.impl.FuturePriceDataService;
import com.javaweb.service.impl.SpotPriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Math.abs;

@Service
public class TriggerService {
    @Autowired
    private SseHelper sseHelper;

    @Autowired
    private TriggerCheckHelper triggerCheckHelper;

    @Autowired
    private FundingRateWebSocketService fundingRateWebSocketService;

    @Autowired
    private FundingRateDataService fundingRateDataService;

    @Autowired
    private FundingIntervalWebService fundingIntervalWebService;

    @Autowired
    private SpotWebSocketService spotWebSocketService;

    @Autowired
    private FutureWebSocketService futureWebSocketService;

    @Autowired
    private SpotPriceDataService spotPriceDataService;

    @Autowired
    private FuturePriceDataService futurePriceDataService;

    public SseEmitter handleStreamComparePrice(List<String> symbols,String username) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        spotWebSocketService.connectToWebSocket(symbols);
        futureWebSocketService.connectToWebSocket(symbols);

        Map<String, PriceDTO> spotPriceDataMap = spotPriceDataService.getPriceDataMap();
        Map<String, PriceDTO> futurePriceDataMap = futurePriceDataService.getPriceDataMap();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            List<String> firedSymbols = triggerCheckHelper.checkCompareSymbolndTriggerAlert(symbols, spotPriceDataMap, futurePriceDataMap, username);

            if (!firedSymbols.isEmpty()) {
                for (String symbol : firedSymbols) {
                    System.out.println("Trigger fired for symbol: " + symbol);
                    try {
                        sseEmitter.send(SseEmitter.event().name("CompareSpotAndFuture").data("Trigger fired for username: " + username + " with symbol: " + symbol));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                System.out.println("No trigger fired for symbol: " + symbols);
            }
        }, 0, 1, TimeUnit.SECONDS);
        return sseEmitter;
    }

    public SseEmitter handleStreamPrice(String priceType, List<String> symbols, String username, Map<String, PriceDTO> priceDataMap, WebSocketConfig webSocketConfig) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, priceDataMap, priceType, username);

            if(!firedSymbols.isEmpty()) {
                for(String symbol : firedSymbols) {
                    System.out.println("Triggers fired for user: " + username + " for symbols: " + symbol);
                    try {
                        sseEmitter.send(SseEmitter.event().name("trigger").data("Trigger fired for username: " + username + " with symbol: " + symbol));
                    }
                    catch (Exception e ) {
                        sseEmitter.completeWithError(e);
                    }
                }
            } else {
                System.out.println("No triggers fired for user: " + username + " for symbols: " + symbols);
            }
        }, 0, 5, TimeUnit.SECONDS); //trigger mỗi 5s

        for (String symbol : symbols) {
            sseHelper.createPriceSseEmitter(sseEmitter, priceType, symbol, priceDataMap, webSocketConfig);
        }

        return sseEmitter;
    }

    public SseEmitter handleStreamFundingRate(List<String> symbols, String username, WebSocketConfig webSocketConfig) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        scheduleFundingIntervalDataUpdate(symbols);

        fundingRateWebSocketService.connectToWebSocket(symbols);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataMap();
            List<Map<String, FundingIntervalDTO>> fundingIntervalDataList = fundingIntervalWebService.getLatestFundingIntervalData(symbols);

            FundingRateAndIntervalHelper.streamCombinedData(sseEmitter, symbols, fundingRateDataMap, fundingIntervalDataList);

            List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, fundingRateDataMap, "FundingRate", username);

            if(!firedSymbols.isEmpty()) {
                for(String symbol : firedSymbols) {
                    System.out.println("Triggers fired for user: " + username + " for symbols: " + symbol);
                    try {
                        sseEmitter.send(SseEmitter.event().name("trigger").data("Trigger fired for username: " + username + " with symbol: " + symbol));
                    }
                    catch (Exception e ) {
                        sseEmitter.completeWithError(e);
                    }
                }
            } else {
                System.out.println("No triggers fired for user: " + username + " for symbols: " + symbols);
            }
        }, 0, 5, TimeUnit.SECONDS); //trigger mỗi 5s

//        for (String symbol : symbols) {
//            sseHelper.createFundingRateSseEmitter(sseEmitter, priceType, symbol, fundingRateDataMap, webSocketConfig);
//        }

        return sseEmitter;
    }



    private double getSpotPrice(String symbol, Map<String, PriceDTO> priceDataMap) {
        PriceDTO priceDTO = priceDataMap.get(symbol);
        if (priceDTO != null && priceDTO.getPrice() != null) {
            try {
                return Double.parseDouble(priceDTO.getPrice());
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format for symbol: " + symbol + ", returning 0.0");
                return 0.0;
            }
        }
        return 0.0;
    }

    private double getFuturePrice(String symbol, Map<String, PriceDTO> priceDataMap) {
        PriceDTO priceDTO = priceDataMap.get(symbol);
        if (priceDTO != null && priceDTO.getPrice() != null) {
            try {
                return Double.parseDouble(priceDTO.getPrice());
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format for symbol: " + symbol + ", returning 0.0");
                return 0.0;
            }
        }
        return 0.0;
    }

    public void scheduleFundingIntervalDataUpdate(List<String> symbols) {
        ScheduledExecutorService dataUpdater = Executors.newScheduledThreadPool(1);
        dataUpdater.scheduleAtFixedRate(() -> {
            fundingIntervalWebService.getLatestFundingIntervalData(symbols);
            System.out.println("FundingInterval data updated for symbols: " + symbols);
        }, 0, 15, TimeUnit.MINUTES);
    }
}