package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.service.DateTimeHelper;
import com.javaweb.helpers.service.PriceDTOHelper;
import com.javaweb.helpers.sse.SseHelper;
import com.javaweb.helpers.trigger.SnoozeCheckHelper;
import com.javaweb.helpers.trigger.TriggerCheckHelper;
import com.javaweb.service.IPriceDataService;
import com.javaweb.service.snooze.SpotSnoozeConditionService;
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
    @Autowired
    private SpotSnoozeConditionService spotSnoozeConditionService;
    private Map<String, PriceDTO> spotPriceDataMap = new ConcurrentHashMap<>();
    @Autowired
    SnoozeCheckHelper snoozeCheckHelper;
    @Override
    public void handleWebSocketMessage(JsonNode data) {
        long eventTimeLong = data.get("E").asLong();
        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);

        String symbol = data.get("s").asText();
        String price = data.get("c").asText();

        PriceDTO priceDTO = PriceDTOHelper.createPriceDTO(symbol, price, eventTime);

        spotPriceDataMap.put("Spot Price: " + symbol, priceDTO);

        /************************** Trigger ***********************************/
        // Kiểm tra xem snooze có đang hoạt động cho trigger này không

        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(Arrays.asList(symbol), "spot");
        // Nếu không có snooze hoạt động và điều kiện được đáp ứng, gửi thông báo qua SSE
        boolean conditionMet = triggerCheckHelper.checkSymbolAndTriggerAlert(Arrays.asList(symbol), spotPriceDataMap, "spot");
        if (conditionMet) {
            SseEmitter emitter = sseHelper.getSseEmitterBySymbol(symbol, "Spot");
            if (emitter != null) {
                try {


                    if (snoozeActive ) {
                        System.out.println("Snooze is active, not sending alert for symbol: " + symbol);
                    } else {
                        // Nếu snooze không hoạt động, gửi thông báo qua emitter
                        System.out.println("hello"+ snoozeActive);
                        emitter.send(SseEmitter.event().name("price-alert").data("spot price condition met for " + symbol));
                    }
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            } else {
                System.out.println("No emitter found for symbol: " + symbol);
            }
        }
    }

    public Map<String, PriceDTO> getPriceDataMap() {
        return spotPriceDataMap;
    }
}
