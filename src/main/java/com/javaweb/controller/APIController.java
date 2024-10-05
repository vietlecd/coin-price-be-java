package com.javaweb.controller;

import com.javaweb.model.PriceDTO;
import com.javaweb.service.BinanceWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api")
public class APIController {

    private final Map<String, PriceDTO> priceDataMap = new ConcurrentHashMap<>();

    @Autowired
    private BinanceWebSocketService binanceWebSocketService;

    @GetMapping("/get-kline")
    public SseEmitter streamSpotPrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        binanceWebSocketService.connectToWebSocket(symbols);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(priceDataMap);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

        emitter.onCompletion(() -> {
            executor.shutdown();
            binanceWebSocketService.closeWebSocket();
        });

        emitter.onTimeout(() -> {
            executor.shutdown();
            binanceWebSocketService.closeWebSocket();
        });

        emitter.onError((ex) -> {
            executor.shutdown();
            binanceWebSocketService.closeWebSocket();
        });

        return emitter;
    }

    public void updateKlineData(String symbol, String openPrice, String closePrice, String highPrice, String lowPrice, String volume, String numberOfTrades,
                                String isKlineClosed,
                                String baseAssetVolume,
                                String takerBuyVolume,
                                String takerBuyBaseVolume,
                                String eventTime,
                                String klineStartTime,
                                String klineCloseTime ) {
        priceDataMap.put(symbol.toUpperCase(), new PriceDTO(symbol,
                openPrice,
                closePrice,
                highPrice,
                lowPrice,
                volume,
                numberOfTrades,
                isKlineClosed,
                baseAssetVolume,
                takerBuyVolume,
                takerBuyBaseVolume,
                eventTime,
                klineStartTime,
                klineCloseTime));
    }

}
