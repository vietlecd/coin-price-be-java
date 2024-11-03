package com.javaweb.connect.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.IConnectToWebSocketService;
import com.javaweb.service.impl.SpotPriceDataService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpotWebSocketService extends TextWebSocketHandler implements IConnectToWebSocketService {
    private final Set<String> subscribedSymbols = ConcurrentHashMap.newKeySet();
    private final SpotPriceDataService spotPriceDataService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketConfig webSocketConfig;
    private final WebSocketClient webSocketClient;


    private String buildSpotWebSocketUrl(Set<String> streams) {
        String streamParam = streams.stream().map(s -> s.toLowerCase() + "@ticker").collect(Collectors.joining("/"));
        return "wss://stream.binance.com:9443/stream?streams=" + streamParam;
    }

    private boolean isConnecting = false;

    public synchronized void connectToWebSocket(List<String> streams, boolean isTriggerRequest) {

        boolean hasNewSymbols = subscribedSymbols.addAll(streams);
        boolean shouldReconnect = hasNewSymbols || webSocketConfig.isSessionClosed();

        if (shouldReconnect && !isConnecting) {
            isConnecting = true;
            try {
                String wsUrl = buildSpotWebSocketUrl(subscribedSymbols);
                // Truyền cờ isTriggerRequest trực tiếp vào handler
                webSocketConfig.closeWebSocketSession();
                webSocketConfig.connectToWebSocket(wsUrl, webSocketClient, new SpotWebSocketHandler(isTriggerRequest));
            } finally {
                isConnecting = false;
            }
        }
    }

    private class SpotWebSocketHandler extends TextWebSocketHandler {

        private final boolean isTriggerRequest;

        public SpotWebSocketHandler(boolean isTriggerRequest) {
            this.isTriggerRequest = isTriggerRequest;
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            String payload = message.getPayload();
            JsonNode data = objectMapper.readTree(payload).get("data");

            spotPriceDataService.handleWebSocketMessage(data, isTriggerRequest);
        }
    }
}
