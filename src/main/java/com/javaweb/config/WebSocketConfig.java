package com.javaweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutionException;

import static org.apache.tomcat.jni.Socket.close;
import static org.apache.tomcat.jni.Socket.shutdown;

@Configuration
public class WebSocketConfig {

    private WebSocketSession webSocketSession;

    @Bean
    public WebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }

    public void connectToWebSocket(String webSocketUrl, WebSocketClient webSocketClient, TextWebSocketHandler handler) {
        try {
            this.webSocketSession = webSocketClient.doHandshake(handler, webSocketUrl).get();
            System.out.println("Connected to WebSocket at: " + webSocketUrl);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void closeWebSocket() {
        try {
            webSocketSession.close();
            if (this.webSocketSession != null && this.webSocketSession.isOpen()) {
                this.webSocketSession.close();
                System.out.println("WebSocket disconnected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
