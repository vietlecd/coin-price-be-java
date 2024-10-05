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

    // Thay đổi để hỗ trợ kline thay vì ticker
    private String buildWebSocketUrl(List<String> streams) {
        String streamParam = streams.stream().map(s -> s.toLowerCase() + "@kline_1m").collect(Collectors.joining("/"));
        return "wss://stream.binance.com:9443/stream?streams=" + streamParam;
    }

    @Override
    public void connectToWebSocket(List<String> streams) {
        String wsUrl = buildWebSocketUrl(streams);

        StandardWebSocketClient client = new StandardWebSocketClient();
        try {
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

        // Lấy dữ liệu từ kline
        JsonNode data = objectMapper.readTree(payload).get("data").get("k");

        String symbol = data.get("s").asText();
        String openPrice = data.get("o").asText();   // Giá mở
        String closePrice = data.get("c").asText();  // Giá đóng
        String highPrice = data.get("h").asText();   // Giá cao nhất
        String lowPrice = data.get("l").asText();    // Giá thấp nhất


        System.out.println("Symbol: " + symbol +  ", Open Price: " + openPrice + ", Close Price: " + closePrice +
                ", High Price: " + highPrice + ", Low Price: " + lowPrice );

        // Giả sử apiController được cập nhật để xử lý dữ liệu Kline
        apiController.updateKlineData(symbol, openPrice, closePrice, highPrice, lowPrice);
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
