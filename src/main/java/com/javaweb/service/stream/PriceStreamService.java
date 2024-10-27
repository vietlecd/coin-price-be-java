//package com.javaweb.service.stream;
//
////import com.javaweb.dto.KlineDTO;
//import com.javaweb.dto.PriceDTO;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.*;
//
//@Service
//public class PriceStreamService {
//
//    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
//    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
//    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
//
//    public SseEmitter createSseEmitter(SseEmitter emitter, String type, String symbol,
//                                       Runnable sendTask, Map<String, PriceDTO> priceDataMap,
//                                       Map<String, KlineDTO> klineDataMap) {
//        String key = type + " Price: " + symbol.toUpperCase();
//        System.out.println(key);
//
//        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(sendTask, 0, 1, TimeUnit.SECONDS);
//        scheduledTasks.put(key, scheduledFuture);
//        emitters.put(key, emitter);
//
//        Runnable cancelTask = () -> {
//            if (scheduledTasks.containsKey(key)) {
//                ScheduledFuture<?> future = scheduledTasks.get(key);
//                if (future != null && !future.isCancelled()) {
//                    future.cancel(true);
//                    scheduledTasks.remove(key);
//                    System.out.println("Cancelled scheduled task for type: " + key);
//                }
//            }
//
//            if (emitters.containsKey(key)) {
//                emitters.remove(key);
//                System.out.println("Removed SSE emitter for type: " + key);
//            }
//
//            // Xóa dữ liệu của symbol khỏi priceDataMap khi kết nối SSE bị đóng
//            if (priceDataMap != null) {
//                priceDataMap.remove(key);
//                System.out.println("Removed price data for symbol: " + symbol);
//            }
//
//            if (klineDataMap != null) {
//                klineDataMap.remove("Kline Price: " + symbol);
//                System.out.println("Removed kline data for symbol: " + symbol);
//            }
//
//            System.out.println("Connection closed and data cleared for type: " + key);
//        };
//
//        emitter.onCompletion(cancelTask);
//        emitter.onTimeout(cancelTask);
//        emitter.onError((ex) -> cancelTask.run());
//
//        return emitter;
//    }
//
//    public SseEmitter createPriceSseEmitter(SseEmitter emitter, String type, String symbol,
//                                            Map<String, PriceDTO> priceDataMap) {
//        Runnable sendPriceTask = () -> {
//            try {
//                PriceDTO priceDTO = priceDataMap.get(type + " Price: " + symbol.toUpperCase());
//                if (priceDTO != null) {
//                    emitter.send(priceDTO);
//                }
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//            }
//        };
//
//        return createSseEmitter(emitter, type, symbol, sendPriceTask, priceDataMap, null);
//    }
//
//    public SseEmitter createKlineSseEmitter(SseEmitter emitter, String type, String symbol,
//                                            Map<String, KlineDTO> klineDataMap) {
//        Runnable sendKlineTask = () -> {
//            try {
//                KlineDTO klineDTO = klineDataMap.get("Kline Price: " + symbol);
//                if (klineDTO != null) {
//                    emitter.send(klineDTO);
//                }
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//            }
//        };
//
//        return createSseEmitter(emitter, type, symbol, sendKlineTask, null, klineDataMap);
//    }
//
//    public void closeAllWebSockets() {
//        for (Map.Entry<String, ScheduledFuture<?>> entry : scheduledTasks.entrySet()) {
//            ScheduledFuture<?> future = entry.getValue();
//            future.cancel(true);
//        }
//        scheduledTasks.clear();
//
//        for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
//            SseEmitter emitter = entry.getValue();
//            if (emitter != null) {
//                emitter.complete();
//            }
//        }
//        emitters.clear();
//
//    }
//}
