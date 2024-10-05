package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.service.PriceDataService;
import com.javaweb.service.SpotWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotWebSocketServiceImpl extends TextWebSocketHandler implements SpotWebSocketService {

    @Autowired
    private PriceDataService priceDataService;

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

        long eventTime = data.get("E").asLong();

        Instant instant = Instant.ofEpochMilli(eventTime);
        ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String symbol = data.get("s").asText();
        String price = data.get("c").asText();

        System.out.println("Event Time: " + formattedDateTime + "Symbol: " + symbol + ", Spot Price: " + price);

        priceDataService.updatePriceData(formattedDateTime, symbol, price);
    }
}
