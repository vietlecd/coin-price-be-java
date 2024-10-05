package com.javaweb.controller;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.model.PriceDTO;
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

    private final Map<String, PriceDTO> priceDataMap = new ConcurrentHashMap<>();

    @Autowired
    private ScheduledExecutorService executor;

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private SpotWebSocketService spotWebSocketService;

    @Autowired
    private PriceDataService priceDataService;

//    @Autowired
//    private FutureWebSocketService futureWebSocketService;

    @GetMapping("/get-spot-price")
    public SseEmitter streamSpotPrices(@RequestParam List<String> symbols) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        spotWebSocketService.connectToSpotWebSocket(symbols);

        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(priceDataService.getPriceDataMap());
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

        Runnable cancelTask = () -> {
            scheduledFuture.cancel(true);
            webSocketConfig.closeWebSocket();
        };

        emitter.onCompletion(cancelTask);
        emitter.onTimeout(cancelTask);
        emitter.onError((ex) -> cancelTask.run());

        return emitter;
    }

}

//    @GetMapping("/get-future-price")
//    public SseEmitter streamFuturePrices(@RequestParam List<String> symbols) {
//        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
//
//        futureWebSocketService.connectToFutureWebSocket(symbols);
//
//        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
//            try {
//                emitter.send(priceDataService.getPriceDataMap());
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//            }
//        }, 0, 1, TimeUnit.SECONDS);
//
//        Runnable cancelTask = () -> {
//            scheduledFuture.cancel(true);
//            webSocketConfig.closeWebSocket();
//        };
//
//        emitter.onCompletion(cancelTask);
//        emitter.onTimeout(cancelTask);
//        emitter.onError((ex) -> cancelTask.run());
//
//        return emitter;
//    }
//
//}
//
//

