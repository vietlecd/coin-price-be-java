package com.javaweb.connect.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.ISpotWebSocketService;
import com.javaweb.service.ISpotPriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotWebSocketService extends TextWebSocketHandler implements ISpotWebSocketService {

    @Autowired
    private ISpotPriceDataService spotPriceDataService;

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

    @Override
    public void connectToSpotWebSocket(List<String> streams) {
        String wsUrl = buildSpotWebSocketUrl(streams);
        webSocketConfig.connectToWebSocket(wsUrl, webSocketClient, this);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received from Binance: " + payload);

        JsonNode data = objectMapper.readTree(payload).get("data");

        spotPriceDataService.handleSpotWebSocketMessage(data);
    }

    public void closeWebSocket() {
        webSocketConfig.closeWebSocket();
        System.out.println("WebSocket closed from service.");
    }

}
