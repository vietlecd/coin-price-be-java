package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.Service.DateTimeHelper;
import com.javaweb.helpers.Service.PriceDTOHelper;
import com.javaweb.service.ISpotPriceDataService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SpotPriceDataService implements ISpotPriceDataService {

    private final Map<String, PriceDTO> spotPriceDataMap = new ConcurrentHashMap<>();

    @Override
    public void handleSpotWebSocketMessage(JsonNode data) {
        long eventTimeLong = data.get("E").asLong();
        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);

        String symbol = data.get("s").asText();
        String price = data.get("c").asText();

        System.out.println("Event Time: " + eventTime + ", Symbol: " + symbol + ", Spot Price: " + price);

        PriceDTO priceDTO = PriceDTOHelper.createPriceDTO(symbol, price, eventTime);
        spotPriceDataMap.put("SpotPrice:", priceDTO);
    }

    public Map<String, PriceDTO> getSpotPriceDataMap(){
            return spotPriceDataMap;
    }

}
