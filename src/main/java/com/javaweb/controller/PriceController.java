package com.javaweb.controller;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.model.FundingRateDTO;
import com.javaweb.model.PriceDTO;
import com.javaweb.service.FundingRateWebSocketService;
import com.javaweb.service.FutureWebSocketService;
import com.javaweb.service.PriceDataService;
import com.javaweb.service.SpotWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api")
public class PriceController {

    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();


    @Autowired
    private ScheduledExecutorService executor;

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private SpotWebSocketService spotWebSocketService;

    @Autowired
    private PriceDataService priceDataService;

    @Autowired
    private FutureWebSocketService futureWebSocketService;

    @Autowired
    private FundingRateWebSocketService fundingRateWebSocketService;

    @GetMapping("/get-spot-price")
    public SseEmitter streamSpotPrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        spotWebSocketService.connectToSpotWebSocket(symbols);

        return getPriceSseEmitter(emitter, "spot");
    }


    @GetMapping("/get-future-price")
    public SseEmitter streamFuturePrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        futureWebSocketService.connectToFutureWebSocket(symbols);


        return getPriceSseEmitter(emitter, "future");
    }

    @GetMapping("/get-funding-rate")
    public SseEmitter streamFundingRate(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        fundingRateWebSocketService.connectToFundingRateWebSocket(symbols);

        return getFundingRateSseEmitter(emitter, "funding-rate");
    }

    private SseEmitter getPriceSseEmitter(SseEmitter emitter, String type) {
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
            try {
                Map<String, PriceDTO> priceData = priceDataService.getPriceDataMap();

                emitter.send(priceData);

            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

        scheduledTasks.put(type, scheduledFuture);
        emitters.put(type, emitter);

        Runnable cancelTask = () -> {
            scheduledFuture.cancel(true);
            webSocketConfig.closeWebSocket();
        };

        emitter.onCompletion(cancelTask);
        emitter.onTimeout(cancelTask);
        emitter.onError((ex) -> cancelTask.run());

        return emitter;
    }

    private SseEmitter getFundingRateSseEmitter(SseEmitter emitter, String type) {
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
            try {
                Map<String, FundingRateDTO> fundingRateData  = priceDataService.getFundingRateDataMap();

                emitter.send(fundingRateData);

            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

        scheduledTasks.put(type, scheduledFuture);
        emitters.put(type, emitter);

        Runnable cancelTask = () -> {
            scheduledFuture.cancel(true);
            webSocketConfig.closeWebSocket();
        };

        emitter.onCompletion(cancelTask);
        emitter.onTimeout(cancelTask);
        emitter.onError((ex) -> cancelTask.run());

        return emitter;
    }


    @DeleteMapping("/close-websocket")
    public void closeWebSocket(@RequestParam String type) {
        if (scheduledTasks.containsKey(type)) {
            // Hủy lập lịch
            ScheduledFuture<?> future = scheduledTasks.get(type);
            future.cancel(true);
            scheduledTasks.remove(type);

            // Đóng SseEmitter
            SseEmitter emitter = emitters.get(type);
            if (emitter != null) {
                emitter.complete();
                emitters.remove(type);
            }
        }

        if (type.equalsIgnoreCase("spot")) {
            spotWebSocketService.closeWebSocket();  // Đóng kết nối Spot
        } else if (type.equalsIgnoreCase("future")) {
            futureWebSocketService.closeWebSocket();  // Đóng kết nối Future
        } else if (type.equalsIgnoreCase("funding-rate")) {
            fundingRateWebSocketService.closeWebSocket();  // Đóng kết nối Funding-Rate
        }
    }


}

