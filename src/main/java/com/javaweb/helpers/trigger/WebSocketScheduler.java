package com.javaweb.helpers.trigger;

import com.javaweb.connect.impl.FundingRateWebSocketService;
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

    @Autowired
    private FundingRateWebSocketService fundingRateWebSocketService;

    // Chạy mỗi 5 phút để kiểm tra và kết nối lại WebSocket nếu cần
    @Scheduled(fixedRate = 30000) // 300000 ms = 5 phút
    public void checkAndConnectToWebSocket() {
        // Lấy symbols cho Spot triggers
        List<String> spotSymbolsWithTriggers = triggerSymbolService.getSpotSymbolsWithTriggers();
        if (!spotSymbolsWithTriggers.isEmpty()) {
            System.out.println("Checking Spot WebSocket connections for symbols: " + spotSymbolsWithTriggers);
            spotWebSocketService.connectToWebSocket(spotSymbolsWithTriggers);
        }

        // Lấy symbols cho Future triggers
        List<String> futureSymbolsWithTriggers = triggerSymbolService.getFutureSymbolsWithTriggers();
        if (!futureSymbolsWithTriggers.isEmpty()) {
            System.out.println("Checking Future WebSocket connections for symbols: " + futureSymbolsWithTriggers);
            futureWebSocketService.connectToWebSocket(futureSymbolsWithTriggers);
        }

        // Lấy symbols cho FundingRate triggers
        List<String> fundingRateSymbolsWithTriggers = triggerSymbolService.getFundingRateSymbolsWithTriggers();
        if (!fundingRateSymbolsWithTriggers.isEmpty()) {
            System.out.println("Checking FundingRate WebSocket connections for symbols: " + fundingRateSymbolsWithTriggers);
            fundingRateWebSocketService.connectToWebSocket(fundingRateSymbolsWithTriggers);
        }
    }
}
