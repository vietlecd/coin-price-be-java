package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.controller.APIController;
import com.javaweb.service.BinanceWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BinanceWebSocketServiceImpl extends TextWebSocketHandler implements BinanceWebSocketService {


    @Autowired
    private APIController apiController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private WebSocketSession webSocketSession;

    private String buildWebSocketUrl(List<String> streams) {
        String streamParam = streams.stream().collect(Collectors.joining("/"));
        return "wss://stream.binance.com:9443/stream?streams=" + streamParam;
    }

    @Override
    public void connectToWebSocket(List<String> streams) {
        String wsUrl = buildWebSocketUrl(streams);
        StandardWebSocketClient client = new StandardWebSocketClient();
        try {
            // Connect to the dynamically built WebSocket URL
            this.webSocketSession = client.doHandshake(this, wsUrl).get();
            System.out.println("Connected to Binance WebSocket at: " + wsUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received from Binance: " + payload);

        JsonNode data = objectMapper.readTree(payload).get("data");

        String symbol = data.get("s").asText();
        String price = data.get("c").asText();

        System.out.println("Symbol: " + symbol + ", Price: " + price);

        apiController.updatePriceData(symbol, price);

    }

    @Override
    public void closeWebSocket() {
        if (this.webSocketSession != null && this.webSocketSession.isOpen()) {
            try {
                this.webSocketSession.close();
                System.out.println("WebSocket session closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
