package com.javaweb.service.trigger;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.sse.SseHelper;
import com.javaweb.helpers.trigger.TriggerCheckHelper;
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

    public SseEmitter handleStreamComparePrice(List<String> symbols, Map<String, PriceDTO> spotPriceDataMap, Map<String, PriceDTO> futurePriceDataMap, String username) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

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

    public SseEmitter handleStreamFundingRate(String priceType, List<String> symbols, String username, Map<String, FundingRateDTO> fundingRateDataMap, WebSocketConfig webSocketConfig) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, fundingRateDataMap, priceType, username);

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
            sseHelper.createFundingRateSseEmitter(sseEmitter, priceType, symbol, fundingRateDataMap, webSocketConfig);
        }

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


}
