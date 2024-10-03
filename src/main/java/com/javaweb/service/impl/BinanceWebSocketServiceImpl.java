package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.model.PriceDTO;
import com.javaweb.service.BinanceWebSocketService;
import com.javaweb.service.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;

@Service
public class BinanceWebSocketServiceImpl extends TextWebSocketHandler implements BinanceWebSocketService {

    private final String BINANCE_WS_URL = "wss://stream.binance.com:9443/ws/btcusdt@ticker";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DataStorageService dataStorageService;

    private WebSocketSession webSocketSession;

    @Override
    @PostConstruct
    public void connectToWebSocket() {
        StandardWebSocketClient client = new StandardWebSocketClient();
        try {
            this.webSocketSession = client.doHandshake(this, BINANCE_WS_URL).get();
            System.out.println("Connected to Binance WebSocket at: " + BINANCE_WS_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received from Binance: " + payload);

        // Parse JSON
        JsonNode jsonNode = objectMapper.readTree(payload);
        String symbol = jsonNode.get("s").asText();
        String price = jsonNode.get("c").asText();

        System.out.println("Symbol: " + symbol + ", PriceEntity: " + price);

        PriceDTO priceDTO = new PriceDTO(symbol, price);

        dataStorageService.savePrice(priceDTO);
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
