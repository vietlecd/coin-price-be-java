package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.helpers.service.DateTimeHelper;
import com.javaweb.helpers.service.FundingRateDTOHelper;
import com.javaweb.helpers.sse.SseHelper;
import com.javaweb.helpers.trigger.TriggerCheckHelper;
import com.javaweb.service.IFundingRateDataService;
import com.javaweb.service.IPriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class FundingRateDataService implements IFundingRateDataService {
    @Autowired
    private TriggerCheckHelper triggerCheckHelper;
    @Autowired
    private SseHelper sseHelper;
    private final Map<String, FundingRateDTO> fundingRateDataMap = new ConcurrentHashMap<>();

    public void handleFundingRateWebSocketMessage(JsonNode data) {
        long eventTimeLong = data.get("E").asLong();
        long nextFundingTime = data.get("T").asLong();

        String eventTime = DateTimeHelper.formatEventTime(eventTimeLong);

        String symbol = data.get("s").asText();
        String fundingRate = data.get("r").asText();

        long countdownInSeconds = (nextFundingTime - eventTimeLong) / 1000;

        String fundingCountdown = String.format("%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(countdownInSeconds),
                TimeUnit.SECONDS.toMinutes(countdownInSeconds) % 60,
                countdownInSeconds % 60
        );

//        System.out.println("Symbol: " + symbol);
//        System.out.println("Funding Rate: " + fundingRate);
//        System.out.println("Funding Rate Countdown: " + fundingCountdown);
//        System.out.println("Event Time: " + eventTime);

        FundingRateDTO fundingRateDTO = FundingRateDTOHelper.createFundingRateDTO(symbol, fundingRate, fundingCountdown, eventTime);

        fundingRateDataMap.put("FundingRate Price: " + symbol, fundingRateDTO);

        boolean conditionMet = triggerCheckHelper.checkSymbolAndTriggerAlert(Arrays.asList(symbol), fundingRateDataMap, "FundingRate");

        if (conditionMet) {
            // Nếu có SseEmitter thì gửi thông báo qua SSE
            SseEmitter emitter = sseHelper.getSseEmitterBySymbol(symbol, "FundingRate");
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event().name("price-alert").data("FundingRate price condition met for " + symbol));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            } else {
                //ystem.out.println("No emitter found for symbol: " + symbol);
            }
        }

    }

    public Map<String, FundingRateDTO> getFundingRateDataMap() {
        return fundingRateDataMap;
    }
}
