package com.javaweb.connect.impl;

import com.javaweb.service.trigger.BinanceTokenListingService;
import com.javaweb.service.webhook.TelegramNotificationService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

@ClientEndpoint
@Service
public class ListingWebSocketService {

    private Session userSession = null;
    private BinanceTokenListingService tokenService;
    private TelegramNotificationService telegramNotificationService;
    private Timer timer;

    public ListingWebSocketService(BinanceTokenListingService tokenService, TelegramNotificationService telegramNotificationService) {
        this.tokenService = tokenService;
        this.telegramNotificationService = telegramNotificationService;
    }

    @PostConstruct
    public void startWebSocketClient() {
        try {
            URI uri = new URI("wss://stream.binance.com:9443/ws"); // Binance WebSocket URL
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, uri); // Connect to Binance WebSocket
            startTokenCheck(); // Start checking for new token listings
            System.out.println("WebSocket client started on application startup.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
        System.out.println("WebSocket connection opened.");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received message: " + message);

        // Check for new token listings
        if (message.contains("NEW LISTING")) {
            tokenService.handleNewToken(message); // Handle new token using the service
            telegramNotificationService.sendTriggerNotification("New token listed: " + message); // Send notification
        }
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("WebSocket connection closed: " + reason);
        this.userSession = null;
        stopTokenCheck();
    }

    @OnError
    public void onError(Session userSession, Throwable throwable) {
        System.out.println("WebSocket error: " + throwable.getMessage());
    }

    private void startTokenCheck() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (userSession != null && userSession.isOpen()) {
                    System.out.println("Checking for new token listings...");
                }
            }
        }, 0, 10000); // Check every 10 seconds
    }

    private void stopTokenCheck() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
