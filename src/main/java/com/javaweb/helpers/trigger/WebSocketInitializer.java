package com.javaweb.helpers.trigger;

import com.javaweb.connect.impl.FundingRateWebSocketService;
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
    private FundingRateWebSocketService fundingRateWebSocketService;

    @Autowired
    private TriggerSymbolService triggerSymbolService;

    @PostConstruct
    public void initWebSocketConnections() {
        // Lấy tất cả symbols có triggers
        List<String> spotSymbols = triggerSymbolService.getSpotSymbolsWithTriggers();
        List<String> futureSymbols = triggerSymbolService.getFutureSymbolsWithTriggers();
        List<String> fundingRateSymbols = triggerSymbolService.getFundingRateSymbolsWithTriggers();

        // Khởi động WebSocket cho Spot symbols
        if (!spotSymbols.isEmpty()) {
            System.out.println("Initializing Spot WebSocket for symbols: " + spotSymbols);
            spotWebSocketService.connectToWebSocket(spotSymbols);
        }

        // Khởi động WebSocket cho Future symbols
        if (!futureSymbols.isEmpty()) {
            System.out.println("Initializing Future WebSocket for symbols: " + futureSymbols);
            futureWebSocketService.connectToWebSocket(futureSymbols);
        }

        // Khởi động WebSocket cho FundingRate symbols
        if (!fundingRateSymbols.isEmpty()) {
            System.out.println("Initializing FundingRate WebSocket for symbols: " + fundingRateSymbols);
            fundingRateWebSocketService.connectToWebSocket(fundingRateSymbols);
        }
    }
}
