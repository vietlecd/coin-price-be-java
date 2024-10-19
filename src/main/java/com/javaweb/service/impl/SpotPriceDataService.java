package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.service.DateTimeHelper;
import com.javaweb.helpers.service.PriceDTOHelper;
import com.javaweb.helpers.sse.SseHelper;
import com.javaweb.helpers.trigger.TriggerCheckHelper;
import com.javaweb.service.IPriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Arrays;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SpotPriceDataService implements IPriceDataService {
    @Autowired
    private TriggerCheckHelper triggerCheckHelper;
    @Autowired
    private SseHelper sseHelper;

    private Map<String, PriceDTO> spotPriceDataMap = new ConcurrentHashMap<>();

    @Override
    public void handleWebSocketMessage(JsonNode data) {
        long eventTimeLong = data.get("E").asLong();
        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);

        String symbol = data.get("s").asText();
        String price = data.get("c").asText();

        PriceDTO priceDTO = PriceDTOHelper.createPriceDTO(symbol, price, eventTime);

        spotPriceDataMap.put("Spot Price: " + symbol, priceDTO);
    }

    public Map<String, PriceDTO> getPriceDataMap(){
            return spotPriceDataMap;
    }

}
