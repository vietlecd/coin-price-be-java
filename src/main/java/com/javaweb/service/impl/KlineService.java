package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.controller.KlineController;
import com.javaweb.service.IKlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KlineService extends TextWebSocketHandler implements IKlineService {

    @Autowired
    private KlineController klineController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private WebSocketSession webSocketSession;

    // Thay đổi để hỗ trợ kline thay vì ticker
    private String buildWebSocketUrl(List<String> streams) {
        // Create the stream parameter with the correct format and kline interval (e.g., kline_1m)
        String streamParam = streams.stream()
                .map(s -> s.toLowerCase() + "@kline_1m") // Ensure lowercase and append "@kline_1m"
                .collect(Collectors.joining("/")); // Join streams with "/"

        // Return the constructed URL using the base URL wss://fstream.binance.com
        return "wss://fstream.binance.com/stream?streams=" + streamParam;
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
        if (payload == null || payload.isEmpty()) {
            System.err.println("Payload is null or empty");
            return;
        }

        // Lấy dữ liệu từ kline, kiểm tra dữ liệu nhận được có hợp lệ không
        JsonNode rootNode = objectMapper.readTree(payload);
        if (rootNode == null || !rootNode.has("data")) {
            System.err.println("Data field is missing or null in the received payload");
            return;
        }


        // Lấy dữ liệu từ kline
        JsonNode data = objectMapper.readTree(payload).get("data").get("k");

        String symbol = data.get("s").asText();
        String klineStartTime = data.get("t").asText(); // Thời gian bắt đầu K-line
        String klineCloseTime = data.get("T").asText(); // Thời gian kết thúc K-line
        String openPrice = data.get("o").asText();   // Giá mở
        String closePrice = data.get("c").asText();  // Giá đóng
        String highPrice = data.get("h").asText();   // Giá cao nhất
        String lowPrice = data.get("l").asText();    // Giá thấp nhất
        String numberOfTrades = data.get("n").asText(); // Số lượng giao dịch
        String isKlineClosed = data.get("x").asText(); // Nến đã đóng hay chưa
        String baseAssetVolume = data.get("q").asText(); // Khối lượng tài sản cơ sở
        String takerBuyVolume = data.get("V").asText(); // Khối lượng mua của taker
        String takerBuyBaseVolume = data.get("Q").asText(); // Khối lượng mua tài sản cơ sở của taker
        String eventTime = rootNode.has("E") ? rootNode.get("E").asText() : "N/A"; // Thời gian sự kiện
        String volume = data.get("v").asText(); // Khối lượng giao dịch

        System.out.println("Symbol: " + symbol +
                ", Open Price: " + openPrice +
                ", Close Price: " + closePrice +
                ", High Price: " + highPrice +
                ", Low Price: " + lowPrice +
                ", Volume: " + volume +
                ", Number of Trades: " + numberOfTrades +
                ", Is Kline Closed: " + isKlineClosed +
                ", Base Asset Volume: " + baseAssetVolume +
                ", Taker Buy Volume: " + takerBuyVolume +
                ", Taker Buy Base Volume: " + takerBuyBaseVolume +
                ", Event Time: " + eventTime +
                ", Kline Start Time: " + klineStartTime +
                ", Kline Close Time: " + klineCloseTime);

        // Giả sử apiController được cập nhật để xử lý dữ liệu Kline
        klineController.updateKlineData(
                symbol,
                openPrice,
                closePrice,
                highPrice,
                lowPrice,
                volume,
                numberOfTrades,
                isKlineClosed,
                baseAssetVolume,
                takerBuyVolume,
                takerBuyBaseVolume,
                eventTime,
                klineStartTime,
                klineCloseTime
        );
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
