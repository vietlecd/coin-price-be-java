//package com.javaweb.service.impl;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.javaweb.dto.KlineDTO;
//import com.javaweb.helpers.service.DateTimeHelper;
////import com.javaweb.converter.KlineDTOHelper;
//import org.springframework.stereotype.Service;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//public class KlineDataService extends TextWebSocketHandler {
//    private final Map<String, KlineDTO> klineDataMap = new ConcurrentHashMap<>();
//
//
//    public void handleWebSocketMessage(JsonNode data){
//        Long eventTimeLong = data.get("E").asLong();
//
//        String symbol = data.get("s").asText();
//
//        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);
//
//        JsonNode data1 = data.get("k");
//        Long klineStartTimeLong = data1.get("t").asLong();
//        Long klineCloseTimeLong = data1.get("T").asLong();
//        String openPrice = data1.get("o").asText();
//        String closePrice = data1.get("c").asText();
//        String highPrice = data1.get("h").asText();
//        String lowPrice = data1.get("l").asText(); //chú y
//        Long numberOfTrades = data1.get("n").asLong();
//        String baseAssetVolume = data1.get("q").asText(); // Khối lượng tài sản cơ sở
//        String takerBuyVolume = data1.get("V").asText(); // Khối lượng mua của taker
//        String takerBuyBaseVolume = data1.get("Q").asText(); // Khối lượng mua tài sản cơ sở của taker
//        String volume = data1.get("v").asText(); // Khối lượng giao dịch
//
//        String klineStartTime = DateTimeHelper.formatEventTime(klineStartTimeLong);
//        String klineCloseTime = DateTimeHelper.formatEventTime(klineCloseTimeLong);
//
//        // Tạo KlineDTO sử dụng Builder pattern
////        KlineDTO klineDTO = KlineDTOHelper.createKlineDTO(
////                symbol, klineStartTime, klineCloseTime, openPrice, closePrice, highPrice, lowPrice,
////                numberOfTrades, baseAssetVolume, takerBuyVolume, takerBuyBaseVolume, volume, eventTime
////        );
////
////        klineDataMap.put("Kline Price: " + symbol, klineDTO);
//    }
//
//    public Map<String, KlineDTO> getPriceDataMap() {
//        return klineDataMap;
//    }
//}
