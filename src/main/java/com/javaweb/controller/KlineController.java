package com.javaweb.controller;

import com.javaweb.model.KlineDTO;
import com.javaweb.service.IKlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api")
public class KlineController {

    private final Map<String, KlineDTO> priceDataMap = new ConcurrentHashMap<>();

    @Autowired
    private IKlineService IKlineService;

    @GetMapping("/get-kline")
    public SseEmitter KlinePrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        IKlineService.connectToWebSocket(symbols);

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
            IKlineService.closeWebSocket();
        });

        emitter.onTimeout(() -> {
            executor.shutdown();
            IKlineService.closeWebSocket();
        });

        emitter.onError((ex) -> {
            executor.shutdown();
            IKlineService.closeWebSocket();
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
        priceDataMap.put(symbol.toUpperCase(), new KlineDTO(symbol,
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
