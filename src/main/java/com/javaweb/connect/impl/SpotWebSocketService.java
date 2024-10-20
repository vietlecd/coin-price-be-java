package com.javaweb.connect.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.IConnectToWebSocketService;
import com.javaweb.service.impl.SpotPriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotWebSocketService extends TextWebSocketHandler implements IConnectToWebSocketService {

    @Autowired
    private SpotPriceDataService spotPriceDataService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private WebSocketClient webSocketClient;

    private String buildSpotWebSocketUrl(List<String> streams) {
        String streamParam = streams.stream().map(s -> s.toLowerCase() + "@ticker").collect(Collectors.joining("/"));
        return "wss://stream.binance.com:9443/stream?streams=" + streamParam;
    }

    public void connectToWebSocket(List<String> streams, boolean isTriggerRequest) {
        String wsUrl = buildSpotWebSocketUrl(streams);
        // Truyền cờ isTriggerRequest trực tiếp vào handler
        webSocketConfig.connectToWebSocket(wsUrl, webSocketClient, new SpotWebSocketHandler(isTriggerRequest));
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
