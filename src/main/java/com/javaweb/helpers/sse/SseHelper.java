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

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createSseEmitter(SseEmitter emitter, String type,
                                       Runnable sendTask,
                                       WebSocketConfig webSocketConfig) {
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(sendTask, 0, 1, TimeUnit.SECONDS);

        scheduledTasks.put(type, scheduledFuture);
        emitters.put(type, emitter);

        Runnable cancelTask = () -> {
            scheduledFuture.cancel(true);
            //webSocketConfig.closeWebSocket();
        };

        emitter.onCompletion(cancelTask);
        emitter.onTimeout(cancelTask);
        emitter.onError((ex) -> cancelTask.run());

        return emitter;
    }

    public SseEmitter createPriceSseEmitter(SseEmitter emitter, String type,
                                            Map<String, PriceDTO> priceDataMap,
                                            WebSocketConfig webSocketConfig) {
        Runnable sendPriceTask = () -> {
            try {
                emitter.send(priceDataMap);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        };

        return createSseEmitter(emitter, type, sendPriceTask, webSocketConfig);
    }

    public SseEmitter createFundingRateSseEmitter(SseEmitter emitter, String type,
                                                  Map<String, FundingRateDTO> fundingRateDataMap,
                                                  WebSocketConfig webSocketConfig) {
        Runnable sendFundingRateTask = () -> {
            try {
                emitter.send(fundingRateDataMap);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        };

        return createSseEmitter(emitter, type, sendFundingRateTask, webSocketConfig);

    }

    public void closeWebSocket(String type, WebSocketConfig webSocketConfig) {
        if (scheduledTasks.containsKey(type)) {
            ScheduledFuture<?> future = scheduledTasks.get(type);
            //future.cancel(true);
            scheduledTasks.remove(type);

            SseEmitter emitter = emitters.get(type);
            if (emitter != null) {
                emitter.complete();
                emitters.remove(type);
            }
        }

        //webSocketConfig.closeWebSocket();
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
