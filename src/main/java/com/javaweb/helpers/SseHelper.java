package com.javaweb.helpers;

import com.javaweb.DTO.FundingRateDTO;
import com.javaweb.DTO.PriceDTO;
import com.javaweb.config.WebSocketConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class SseHelper {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createPriceSseEmitter(SseEmitter emitter, String type,
                                            Map<String, PriceDTO> priceDataMap,
                                            WebSocketConfig webSocketConfig) {
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(priceDataMap);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

        scheduledTasks.put(type, scheduledFuture);
        emitters.put(type, emitter);

        // Cancel task on emitter completion
        Runnable cancelTask = () -> {
            scheduledFuture.cancel(true);
            webSocketConfig.closeWebSocket();
        };

        emitter.onCompletion(cancelTask);
        emitter.onTimeout(cancelTask);
        emitter.onError((ex) -> cancelTask.run());

        return emitter;
    }

    public SseEmitter createFundingRateSseEmitter(SseEmitter emitter, String type,
                                                  Map<String, FundingRateDTO> fundingRateDataMap,
                                                  WebSocketConfig webSocketConfig) {
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(fundingRateDataMap);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

        scheduledTasks.put(type, scheduledFuture);
        emitters.put(type, emitter);

        // Cancel task on emitter completion
        Runnable cancelTask = () -> {
            scheduledFuture.cancel(true);
            webSocketConfig.closeWebSocket();
        };

        emitter.onCompletion(cancelTask);
        emitter.onTimeout(cancelTask);
        emitter.onError((ex) -> cancelTask.run());

        return emitter;
    }

    public void closeWebSocket(String type, WebSocketConfig webSocketConfig) {
        if (scheduledTasks.containsKey(type)) {
            ScheduledFuture<?> future = scheduledTasks.get(type);
            future.cancel(true);
            scheduledTasks.remove(type);

            // Close the associated emitter
            SseEmitter emitter = emitters.get(type);
            if (emitter != null) {
                emitter.complete();
                emitters.remove(type);
            }
        }

        webSocketConfig.closeWebSocket();
    }
}
