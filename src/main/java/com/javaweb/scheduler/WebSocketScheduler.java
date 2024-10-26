package com.javaweb.scheduler;

import com.javaweb.connect.impl.FundingRateWebSocketService;
import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.service.trigger.TriggerService;
import com.javaweb.service.trigger.TriggerSymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private TriggerService triggerService;

    // Chạy mỗi 1 phút để kiểm tra và kết nối lại WebSocket nếu cần
    @Scheduled(fixedRate = 60000) // 60000 ms = 1 phut
    public void checkAndConnectToWebSocket() {
        Map<String, List<String>> usernamesWithSymbolsForSpot = triggerSymbolService.getUsernamesWithSymbolsSpot();
        Map<String, List<String>> usernamesWithSymbolsForFuture = triggerSymbolService.getUsernamesWithSymbolsFuture();
        Map<String, List<String>> usernamesWithSymbolsForSpotAndFuture = triggerSymbolService.getUsernamesWithSymbolsSpotAndFuture();
        Map<String, List<String>> usernamesWithSymbolsForFundingRate = triggerSymbolService.getUsernamesWithSymbolsFundingRate();

        if (!usernamesWithSymbolsForSpot.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : usernamesWithSymbolsForSpot.entrySet()) {
                String username = entry.getKey();
                List<String> symbols = entry.getValue();

                System.out.println("Checking Spot WebSocket connections for username: " + username + ", symbols: " + symbols);
                spotWebSocketService.connectToWebSocket(symbols, true);

                triggerService.handleAndSendAlertForSpot(symbols, username);
            }
        }

        if (!usernamesWithSymbolsForFuture.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : usernamesWithSymbolsForFuture.entrySet()) {
                String username = entry.getKey();
                List<String> symbols = entry.getValue();

                System.out.println("Checking Future WebSocket connections for username: " + username + ", symbols: " + symbols);
                futureWebSocketService.connectToWebSocket(symbols, true);

                triggerService.handleAndSendAlertForFuture(symbols, username);
            }
        }

        if (!usernamesWithSymbolsForSpotAndFuture.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : usernamesWithSymbolsForSpotAndFuture.entrySet()) {
                String username = entry.getKey();
                List<String> symbols = entry.getValue();

                spotWebSocketService.connectToWebSocket(symbols, true);
                futureWebSocketService.connectToWebSocket(symbols, true);

                triggerService.handleAndSendAlertForPriceDifference(symbols, username);
            }
        }

        if (!usernamesWithSymbolsForFundingRate.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : usernamesWithSymbolsForFundingRate.entrySet()) {
                String username = entry.getKey();
                List<String> symbols = entry.getValue();

                //System.out.println("Checking Future WebSocket connections for username: " + username + ", symbols: " + symbols);
                fundingRateWebSocketService.connectToWebSocket(symbols, true);

                triggerService.handleAndSendAlertForFundingRate(symbols, username);
            }
        }
    }
}
