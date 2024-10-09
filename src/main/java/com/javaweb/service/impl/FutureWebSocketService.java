package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.helper.PriceDTOHelper;
import com.javaweb.service.IFutureWebSocketService;
import com.javaweb.service.IPriceDataService;
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
public class FutureWebSocketService extends TextWebSocketHandler implements IFutureWebSocketService {

    @Autowired
    private IPriceDataService IPriceDataService;

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
        return "wss://fstream.binance.com/stream?streams=" + streamParam;
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

        Long eventTimeLong = data.get("E").asLong();

        String symbol = data.get("s").asText();

        Instant instant = Instant.ofEpochMilli(eventTimeLong);
        ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());
        String eventTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        JsonNode data1 = objectMapper.readTree(payload).get("data").get("k");

        String price = data1.get("c").asText();

        System.out.println("Event Time: " + eventTime + "Symbol: " + symbol + ", Spot Price: " + price);

        IPriceDataService.updatePriceData(PriceDTOHelper.createPriceDTO(eventTime, symbol, price));
    }

    public void closeWebSocket() {
        webSocketConfig.closeWebSocket();
        System.out.println("WebSocket closed from service.");
    }

}
