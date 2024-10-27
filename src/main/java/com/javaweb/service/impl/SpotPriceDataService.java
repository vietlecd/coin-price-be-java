//package com.javaweb.service.impl;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.javaweb.dto.PriceDTO;
//import com.javaweb.helpers.service.DateTimeHelper;
////import com.javaweb.converter.PriceDTOHelper;
//import com.javaweb.service.IPriceDataService;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//public class SpotPriceDataService implements IPriceDataService {
//    private final Map<String, PriceDTO> spotPriceDataUsers = new ConcurrentHashMap<>();
//    private final Map<String, PriceDTO> spotPriceDataTriggers = new ConcurrentHashMap<>();
//
//    @Override
//    public void handleWebSocketMessage(JsonNode data, boolean isTriggered) {
//        long eventTimeLong = data.get("E").asLong();
//        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);
//
//        String symbol = data.get("s").asText();
//        String price = data.get("c").asText();
//
////        PriceDTO priceDTO = PriceDTOHelper.createPriceDTO(symbol, price, eventTime);
//
////        if (!isTriggered) {
////            spotPriceDataUsers.put("Spot Price: " + symbol, priceDTO);
////        }
////        else {
////            spotPriceDataTriggers.put("Spot Price: " + symbol, priceDTO);
////        }
//
//    }
//
//    @Override
//    public Map<String, PriceDTO> getPriceDataUsers(){
//            return spotPriceDataUsers;
//    }
//
//    @Override
//    public Map<String, PriceDTO> getPriceDataTriggers(){
//        return spotPriceDataTriggers;
//    }
//
//
//
//}
