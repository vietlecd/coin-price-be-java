package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.service.DateTimeHelper;
//import com.javaweb.converter.PriceDTOHelper;
import com.javaweb.service.IPriceDataService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FuturePriceDataService implements IPriceDataService {
    private final Map<String, PriceDTO> futurePriceDataUsers = new ConcurrentHashMap<>();
    private final Map<String, PriceDTO> futurePriceDataTriggers = new ConcurrentHashMap<>();

    @Override
    public void handleWebSocketMessage(JsonNode data, boolean isTriggered) {
        Long eventTimeLong = data.get("E").asLong();

        String symbol = data.get("s").asText();

        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);

        JsonNode data1 = data.get("k");

        String price = data1.get("c").asText();

//        PriceDTO priceDTO = PriceDTOHelper.createPriceDTO(symbol, price, eventTime);

//        if (!isTriggered) {
//            futurePriceDataUsers.put("Future Price: " + symbol, priceDTO);
//        }
//        else {
//            futurePriceDataTriggers.put("Future Price: " + symbol, priceDTO);
//        }
    }

    @Override
    public Map<String, PriceDTO> getPriceDataUsers(){
        return futurePriceDataUsers;
    }

    @Override
    public Map<String, PriceDTO> getPriceDataTriggers(){
        return futurePriceDataTriggers;
    }
}
