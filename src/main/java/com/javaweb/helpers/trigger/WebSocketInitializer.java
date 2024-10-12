package com.javaweb.helpers.trigger;

import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.service.trigger.TriggerSymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class WebSocketInitializer {

    @Autowired
    private SpotWebSocketService spotWebSocketService;

    @Autowired
    private FutureWebSocketService futureWebSocketService;

    @Autowired
    private TriggerSymbolService triggerSymbolService;

    @PostConstruct
    public void initWebSocketConnections() {
        // Lấy tất cả symbols có triggers
        List<String> symbolsWithTriggers = triggerSymbolService.getAllSymbolsWithTriggers();

        if (!symbolsWithTriggers.isEmpty()) {
            // Khởi động WebSocket kết nối cho các symbols có triggers
            System.out.println("Initializing WebSocket for symbols: " + symbolsWithTriggers);
            spotWebSocketService.connectToWebSocket(symbolsWithTriggers);  // Tự động kết nối Spot WebSocket
            futureWebSocketService.connectToWebSocket(symbolsWithTriggers); // Tự động kết nối Future WebSocket
        }
    }
}
