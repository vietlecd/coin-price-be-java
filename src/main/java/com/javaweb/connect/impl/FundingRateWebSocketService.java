package com.javaweb.connect.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.IFundingRateWebSocketService;
import com.javaweb.service.IFundingRateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FundingRateWebSocketService extends TextWebSocketHandler implements IFundingRateWebSocketService {

    @Autowired
    private IFundingRateDataService fundingRateDataService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private WebSocketClient webSocketClient;

    // Build the WebSocket URL for funding rate streams
    private String buildFundingRateWebSocketUrl(List<String> streams) {
        String streamParam = streams.stream().map(s -> s.toLowerCase() + "@markPrice@1s").collect(Collectors.joining("/"));
        return "wss://fstream.binance.com/stream?streams=" + streamParam;
    }

    // Connect to WebSocket for Funding Rate
    @Override
    public void connectToFundingRateWebSocket(List<String> streams) {
        String wsUrl = buildFundingRateWebSocketUrl(streams);
        webSocketConfig.connectToWebSocket(wsUrl, webSocketClient, this);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received from Binance: " + payload);

        JsonNode data = objectMapper.readTree(payload).get("data");

        fundingRateDataService.handleFundingRateWebSocketMessage(data);

    }

    public void closeWebSocket() {
        webSocketConfig.closeWebSocket();
        System.out.println("WebSocket closed from service.");
    }
}
