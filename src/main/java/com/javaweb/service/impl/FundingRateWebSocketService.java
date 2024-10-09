package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.helper.DateTimeHelper;
import com.javaweb.helper.FundingRateDTOHelper;
import com.javaweb.service.IFundingRateWebSocketService;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class FundingRateWebSocketService extends TextWebSocketHandler implements IFundingRateWebSocketService {

    @Autowired
    private IPriceDataService IPriceDataService;

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

        long eventTimeLong = data.get("E").asLong();
        long nextFundingTime = data.get("T").asLong();

        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);

        String symbol = data.get("s").asText();
        String fundingRate = data.get("r").asText();

        long countdownInSeconds = (nextFundingTime - eventTimeLong) / 1000;

        String fundingCountdown = String.format("%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(countdownInSeconds),
                TimeUnit.SECONDS.toMinutes(countdownInSeconds) % 60,
                countdownInSeconds % 60
        );

        System.out.println("Symbol: " + symbol);
        System.out.println("Funding Rate: " + fundingRate);
        System.out.println("Funding Rate Countdown: " + fundingCountdown);
        System.out.println("Event Time: " + eventTime);

        IPriceDataService.updateFundingRate(FundingRateDTOHelper.createFundingRateDTO(
                symbol,
                fundingRate,
                fundingCountdown,
                eventTime));
    }

    public void closeWebSocket() {
        webSocketConfig.closeWebSocket();
        System.out.println("WebSocket closed from service.");
    }
}
