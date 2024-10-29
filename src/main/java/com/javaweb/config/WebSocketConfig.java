package com.javaweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.apache.tomcat.jni.Socket.close;
import static org.apache.tomcat.jni.Socket.shutdown;

@Configuration
public class WebSocketConfig {

    private WebSocketSession webSocketSession;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Bean
    public WebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }

    public void connectToWebSocket(String webSocketUrl, WebSocketClient webSocketClient, TextWebSocketHandler handler) {
        //Việt: thêm tính năng check HTTPS
        if (webSocketUrl.startsWith("https://")) {
            webSocketUrl = webSocketUrl.replace("https://", "wss://");
        } else if (webSocketUrl.startsWith("http://")) {
            webSocketUrl = webSocketUrl.replace("http://", "ws://");
        }

        try {
            this.webSocketSession = webSocketClient.doHandshake(handler, webSocketUrl).get();
            System.out.println("Connected to WebSocket at: " + webSocketUrl);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    //Cập nhat phan tắt web socket tránh sự cố
    public void closeWebSocketSession() {
        if (this.webSocketSession != null && this.webSocketSession.isOpen()) {
            try {
                this.webSocketSession.close();
                System.out.println("WebSocket session closed.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @PreDestroy
    public void onDestroy() {
        closeWebSocketSession();
    }
}
