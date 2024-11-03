package com.javaweb.service.stream;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateAndIntervalDTO;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.connect.impl.FundingIntervalWebService;
import com.javaweb.connect.impl.FundingRateWebSocketService;
import com.javaweb.service.impl.FundingRateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.javaweb.converter.FundingRateAndIntervalDTOHelper.combineData;

@Service
public class FundingRateStreamService {

    @Autowired
    private FundingRateWebSocketService fundingRateWebSocketService;

    @Autowired
    private FundingRateDataService fundingRateDataService;

    @Autowired
    private FundingIntervalWebService fundingIntervalWebService;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, FundingRateDTO> fundingRateDataMap = new ConcurrentHashMap<>();

    public SseEmitter handleStreamFundingRate(List<String> symbols) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        String key = "FundingRateAndInterval:" + symbols.hashCode();

        // Kết nối WebSocket
        fundingRateWebSocketService.connectToWebSocket(symbols, false);

        Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataUsers();
        Map<String, FundingIntervalDTO> fundingIntervalDataMap = fundingIntervalWebService.getLatestFundingIntervalData(symbols);

        validateSymbolsExist(symbols, fundingRateDataMap, fundingIntervalDataMap);

        // Cập nhật định kỳ FundingInterval
        scheduleFundingIntervalDataUpdate(symbols);

        Runnable sendTask = () -> {
            try {
                for (String symbol : symbols) {
                    FundingRateDTO fundingRateDTO = fundingRateDataMap.get("FundingRate Price: " + symbol);
                    FundingIntervalDTO fundingIntervalDTO = fundingIntervalDataMap.get(symbol);

                    if (fundingRateDTO != null && fundingIntervalDTO != null) {
                        FundingRateAndIntervalDTO combinedDTO = combineData(fundingRateDTO, fundingIntervalDTO);
                        sseEmitter.send(combinedDTO);
                    }
                }
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        };


        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(sendTask, 0, 1, TimeUnit.SECONDS);
        scheduledTasks.put(key, scheduledFuture);
        emitters.put(key, sseEmitter);

        // Hàm hủy emitter và task
        Runnable cancelTask = () -> {
            System.out.println("Cancelling task for: " + key);

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

            for (String symbol : symbols) {
                String dataKey = "FundingRate Price: " + symbol;
                if (fundingRateDataMap.containsKey(dataKey)) {
                    fundingRateDataMap.remove(dataKey);
                    System.out.println("Removed funding rate data for symbol: " + symbol);
                }
            }
        };

        sseEmitter.onCompletion(cancelTask);
        sseEmitter.onTimeout(cancelTask);
        sseEmitter.onError((ex) -> cancelTask.run());

        return sseEmitter;
    }

    public void scheduleFundingIntervalDataUpdate(List<String> symbols) {
        ScheduledExecutorService dataUpdater = Executors.newScheduledThreadPool(1);
        dataUpdater.scheduleAtFixedRate(() -> {
            fundingIntervalWebService.getLatestFundingIntervalData(symbols);
            System.out.println("FundingInterval data updated for symbols: " + symbols);
        }, 0, 1, TimeUnit.HOURS);
    }

    public void closeAllWebSockets() {
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
    }

    private void validateSymbolsExist(List<String> symbols, Map<String, FundingRateDTO> fundingRateDataMap, Map<String, FundingIntervalDTO> fundingIntervalDataMap) {
        for (String symbol : symbols) {
            if (!fundingRateDataMap.containsKey("FundingRate Price: " + symbol)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funding rate data for symbol " + symbol + " not found");
            }
            if (!fundingIntervalDataMap.containsKey(symbol)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funding interval data for symbol " + symbol + " not found");
            }
        }
    }
}
