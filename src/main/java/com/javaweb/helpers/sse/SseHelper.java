package com.javaweb.helpers.sse;

import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.config.WebSocketConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class SseHelper {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private final Map<String, Map<String, PriceDTO>> priceDataMap = new ConcurrentHashMap<>();

    public SseEmitter createSseEmitter(SseEmitter emitter, String type, String symbol,
                                       Runnable sendTask,
                                       WebSocketConfig webSocketConfig) {
        String key = type + " Price: " + symbol.toUpperCase();
        System.out.println(key);

        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(sendTask, 0, 1, TimeUnit.SECONDS);
        scheduledTasks.put(key, scheduledFuture);
        emitters.put(key, emitter); // Lưu emitter với key đúng định dạng

        Runnable cancelTask = () -> {
            if (scheduledTasks.containsKey(key)) {
                ScheduledFuture<?> future = scheduledTasks.get(key);
                if (future != null && !future.isCancelled()) {
                    future.cancel(true);
                    scheduledTasks.remove(key);
                    System.out.println("Cancelled scheduled task for type: " + key);
                }
            }

            if (emitters.containsKey(key)) {
                emitters.remove(key);
                System.out.println("Removed SSE emitter for type: " + key);
            }

            System.out.println("Connection closed and data cleared for type: " + key);
        };

        emitter.onCompletion(cancelTask);
        emitter.onTimeout(cancelTask);
        emitter.onError((ex) -> cancelTask.run());

        return emitter;
    }

    public SseEmitter createPriceSseEmitter(SseEmitter emitter, String type, String symbol,
                                            Map<String, PriceDTO> priceDataMap,
                                            WebSocketConfig webSocketConfig) {
        Runnable sendPriceTask = () -> {
            try {
                emitter.send(priceDataMap);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        };

        return createSseEmitter(emitter, type, symbol, sendPriceTask, webSocketConfig);
    }

//    public SseEmitter createFundingRateSseEmitter(SseEmitter emitter, String type, String symbol,
//                                                  Map<String, FundingRateDTO> fundingRateDataMap,
//                                                  WebSocketConfig webSocketConfig) {
//        Runnable sendFundingRateTask = () -> {
//            try {
//                emitter.send(fundingRateDataMap);
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//            }
//        };
//
//        return createSseEmitter(emitter, type, symbol, sendFundingRateTask, webSocketConfig);
//
//    }

    public SseEmitter getSseEmitterBySymbol(String symbol) {
       String key = "Spot Price: " + symbol;
        return emitters.get(key);
    }

    public void closeWebSocket(String type, WebSocketConfig webSocketConfig) {
        if (scheduledTasks.containsKey(type)) {
            ScheduledFuture<?> future = scheduledTasks.get(type);
            if (future != null && !future.isCancelled()) {
                future.cancel(true);
                scheduledTasks.remove(type);
                System.out.println("Cancelled scheduled task for type: " + type);
            }
        }

        SseEmitter emitter = emitters.get(type);
        if (emitter != null) {
            emitter.complete();
            emitters.remove(type);
            System.out.println("Closed SSE emitter for type: " + type);
        }

        if (priceDataMap.containsKey(type)) {
            priceDataMap.remove(type);
            System.out.println("Removed price data for type: " + type);
        }
    }


    public void closeAllWebSockets(WebSocketConfig webSocketConfig) {

        for (Map.Entry<String, ScheduledFuture<?>> entry : scheduledTasks.entrySet()) {
            ScheduledFuture<?> future = entry.getValue();
            future.cancel(true);
        }
        scheduledTasks.clear();


        for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
            SseEmitter emitter = entry.getValue();
            if (emitter != null) {
                emitter.complete();
            }
        }
        emitters.clear();


        //webSocketConfig.closeWebSocket();
    }

}
