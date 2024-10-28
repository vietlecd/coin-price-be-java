package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.helpers.service.DateTimeHelper;
import com.javaweb.converter.FundingRateDTOHelper;
import com.javaweb.service.IFundingRateDataService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class FundingRateDataService implements IFundingRateDataService {
    private final Map<String, FundingRateDTO> fundingRateDataUsers = new ConcurrentHashMap<>();
    private final Map<String, FundingRateDTO> fundingRateDataTriggers = new ConcurrentHashMap<>();
    @Override
    public void handleFundingRateWebSocketMessage(JsonNode data,  boolean isTriggered) {
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


        FundingRateDTO fundingRateDTO = FundingRateDTOHelper.createFundingRateDTO(symbol, fundingRate, fundingCountdown, eventTime);

        if (!isTriggered) {
            fundingRateDataUsers.put("FundingRate Price: " + symbol, fundingRateDTO);
        }
        else {
            fundingRateDataTriggers.put("FundingRate Price: " + symbol, fundingRateDTO);
        }


    }

    @Override
    public Map<String, FundingRateDTO> getFundingRateDataUsers() {
        return fundingRateDataUsers;
    }

    @Override
    public Map<String, FundingRateDTO> getFundingRateDataTriggers() {
        return fundingRateDataTriggers;
    }


}
