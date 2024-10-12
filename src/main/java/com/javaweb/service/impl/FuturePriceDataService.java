package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.service.DateTimeHelper;
import com.javaweb.helpers.service.PriceDTOHelper;
import com.javaweb.service.IPriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FuturePriceDataService implements IPriceDataService {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, PriceDTO> futurePriceDataMap = new ConcurrentHashMap<>();

    @Override
    public void handleWebSocketMessage(JsonNode data) {
        Long eventTimeLong = data.get("E").asLong();

        String symbol = data.get("s").asText();

        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);

        JsonNode data1 = data.get("k");

        String price = data1.get("c").asText();

        //System.out.println("Event Time: " + eventTime + "Symbol: " + symbol + ", Future Price: " + price);

        PriceDTO priceDTO = PriceDTOHelper.createPriceDTO(symbol, price, eventTime);
        futurePriceDataMap.put("FuturePrice: " + symbol, priceDTO);
    }

    public Map<String, PriceDTO> getPriceDataMap() {
        return futurePriceDataMap;
    }
}
