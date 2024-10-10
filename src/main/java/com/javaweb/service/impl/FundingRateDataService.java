package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.helpers.Service.DateTimeHelper;
import com.javaweb.helpers.Service.FundingRateDTOHelper;
import com.javaweb.service.IFundingRateDataService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class FundingRateDataService implements IFundingRateDataService {
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

        System.out.println("Symbol: " + symbol);
        System.out.println("Funding Rate: " + fundingRate);
        System.out.println("Funding Rate Countdown: " + fundingCountdown);
        System.out.println("Event Time: " + eventTime);

        FundingRateDTO fundingRateDTO = FundingRateDTOHelper.createFundingRateDTO(symbol, fundingRate, fundingCountdown, eventTime);
        fundingRateDataMap.put("FundingRate:" + symbol, fundingRateDTO);
    }

    public Map<String, FundingRateDTO> getFundingRateDataMap() {
        return fundingRateDataMap;
    }
}
