package com.javaweb.service.impl;

import javax.websocket.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;


@ClientEndpoint
public class BinanceTokenListingService {

    private Session userSession = null;
    private Timer timer;

    public BinanceTokenListingService() {
        // Không khởi tạo ở đây, sẽ khởi tạo từ controller
    }

    public void connect(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
            startTokenCheck();
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

        // Kiểm tra thông điệp để xác định có niêm yết token mới không
        if (message.contains("NEW LISTING")) {
            // Xử lý thông điệp và hiển thị thông tin về token mới
            System.out.println("New token listed: " + message);
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
