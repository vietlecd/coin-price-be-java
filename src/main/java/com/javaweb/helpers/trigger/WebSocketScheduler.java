package com.javaweb.helpers.trigger;

import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.service.trigger.TriggerSymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebSocketScheduler {

    @Autowired
    private TriggerSymbolService triggerSymbolService;

    @Autowired
    private SpotWebSocketService spotWebSocketService;

    @Autowired
    private FutureWebSocketService futureWebSocketService;

    // Chạy mỗi 5 phút (hoặc thời gian bạn muốn)
    @Scheduled(fixedRate = 300000)
    public void checkAndConnectToWebSocket() {
        List<String> symbolsWithTriggers = triggerSymbolService.getAllSymbolsWithTriggers();

        if (!symbolsWithTriggers.isEmpty()) {
            System.out.println("Checking WebSocket connections for symbols: " + symbolsWithTriggers);
            spotWebSocketService.connectToWebSocket(symbolsWithTriggers);  // Kết nối lại cho Spot
            futureWebSocketService.connectToWebSocket(symbolsWithTriggers); // Kết nối lại cho Future
        }
    }
}
