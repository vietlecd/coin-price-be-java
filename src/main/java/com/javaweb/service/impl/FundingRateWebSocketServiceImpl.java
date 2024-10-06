package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.config.WebSocketConfig;
import com.javaweb.helper.FundingRateDTOHelper;
import com.javaweb.model.FundingRateDTO;
import com.javaweb.service.FundingRateWebSocketService;
import com.javaweb.service.PriceDataService;
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
public class FundingRateWebSocketServiceImpl extends TextWebSocketHandler implements FundingRateWebSocketService {

    @Autowired
    private PriceDataService priceDataService;

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

        long eventTime = data.get("E").asLong();
        long nextFundingTime = data.get("T").asLong();

        Instant instant = Instant.ofEpochMilli(eventTime);
        ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String symbol = data.get("s").asText();
        String estimatedSettlePrice = data.get("P").asText();
        String fundingRate = data.get("r").asText();

        long currentTime = System.currentTimeMillis();
        long countdownInSeconds = (nextFundingTime - currentTime) / 1000;

        long intervalInSeconds = (nextFundingTime - eventTime) / 1000;

        String intervalFormatted;
        if (intervalInSeconds <= 4 * 60 * 60) {
            intervalFormatted = "04:00:00";
        } else {
            intervalFormatted = "08:00:00";
        }

        // Convert countdown to HH:mm:ss format
        String countdownFormatted = String.format("%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(countdownInSeconds),
                TimeUnit.SECONDS.toMinutes(countdownInSeconds) % 60,
                countdownInSeconds % 60
        );

        System.out.println("Symbol: " + symbol);
        System.out.println("Funding Rate: " + fundingRate);
        System.out.println("Funding Rate Countdown: " + countdownFormatted);
        System.out.println("Funding Rate Interval: " + intervalFormatted);
        System.out.println("Event Time: " + formattedDateTime);

        priceDataService.updateFundingRate(FundingRateDTOHelper.createFundingRateDTO(
                symbol,
                fundingRate,
                countdownFormatted,
                intervalFormatted,
                formattedDateTime));
    }

    public void closeWebSocket() {
        webSocketConfig.closeWebSocket();
        System.out.println("WebSocket closed from service.");
    }
}
