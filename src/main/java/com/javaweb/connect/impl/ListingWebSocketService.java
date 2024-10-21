package com.javaweb.connect.impl;

import com.javaweb.service.trigger.BinanceTokenListingService;

import javax.websocket.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

@ClientEndpoint
public class ListingWebSocketService {

    private Session userSession = null;
    private BinanceTokenListingService tokenService;
    private Timer timer;

    public ListingWebSocketService(URI endpointURI, BinanceTokenListingService tokenService) {
        this.tokenService = tokenService;
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startTokenCheck();
    }

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
        System.out.println("WebSocket connection opened.");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received message: " + message);

        // Kiểm tra thông điệp để xác định có niêm yết token mới không
        if (message.contains("NEW LISTING")) {
            tokenService.handleNewToken(message); // Gọi service để xử lý token mới
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
        }, 0, 10000); // Kiểm tra mỗi 10 giây
    }

    private void stopTokenCheck() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
