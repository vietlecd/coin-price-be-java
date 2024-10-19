package com.javaweb.helpers.trigger;

import com.javaweb.connect.impl.FundingRateWebSocketService;
import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.service.trigger.TriggerService;
import com.javaweb.service.trigger.TriggerSymbolService;
import com.javaweb.service.webhook.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
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

    // Chạy mỗi 5 phút để kiểm tra và kết nối lại WebSocket nếu cần
    @Scheduled(fixedRate = 30000) // 30000 ms = 30 giay
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
                spotWebSocketService.connectToWebSocket(symbols);

                triggerService.handleAndSendAlertForSpot(symbols, username);
            }
        }

        if (!usernamesWithSymbolsForFuture.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : usernamesWithSymbolsForFuture.entrySet()) {
                String username = entry.getKey();
                List<String> symbols = entry.getValue();

                System.out.println("Checking Future WebSocket connections for username: " + username + ", symbols: " + symbols);
                futureWebSocketService.connectToWebSocket(symbols);

                triggerService.handleAndSendAlertForFuture(symbols, username);
            }
        }

        if (!usernamesWithSymbolsForSpotAndFuture.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : usernamesWithSymbolsForSpotAndFuture.entrySet()) {
                String username = entry.getKey();
                List<String> symbols = entry.getValue();

                //System.out.println("Checking Future WebSocket connections for username: " + username + ", symbols: " + symbols);
                spotWebSocketService.connectToWebSocket(symbols);
                futureWebSocketService.connectToWebSocket(symbols);

                triggerService.handleAndSendAlertForSpotAndFuture(symbols, username);
            }
        }

        if (!usernamesWithSymbolsForFundingRate.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : usernamesWithSymbolsForFundingRate.entrySet()) {
                String username = entry.getKey();
                List<String> symbols = entry.getValue();

                //System.out.println("Checking Future WebSocket connections for username: " + username + ", symbols: " + symbols);
                fundingRateWebSocketService.connectToWebSocket(symbols);

                triggerService.handleAndSendAlertForFundingRate(symbols, username);
            }
        }
    }
}
