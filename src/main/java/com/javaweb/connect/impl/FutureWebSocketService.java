package com.javaweb.connect.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.IFutureWebSocketService;
import com.javaweb.service.IFuturePriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FutureWebSocketService extends TextWebSocketHandler implements IFutureWebSocketService {

    @Autowired
    private IFuturePriceDataService futurePriceDataService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private WebSocketClient webSocketClient;



    private String buildFutureWebSocketUrl(List<String> streams) {
        String streamParam = streams.stream()
                .map(s -> s.toLowerCase() + "@kline_1s")
                .collect(Collectors.joining("/"));
        return "wss://stream.binance.com/stream?streams=" + streamParam;
    }

    @Override
    public void connectToFutureWebSocket(List<String> streams) {
        String wsUrl = buildFutureWebSocketUrl(streams);
        webSocketConfig.connectToWebSocket(wsUrl, webSocketClient, this);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received from Binance: " + payload);

        JsonNode data = objectMapper.readTree(payload).get("data");

        futurePriceDataService.handleFutureWebSocketMessage(data);
    }

    public void closeWebSocket() {
        webSocketConfig.closeWebSocket();
        System.out.println("WebSocket closed from service.");
    }

}
