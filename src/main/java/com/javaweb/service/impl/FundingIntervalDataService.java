package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.helpers.Service.FundingIntervalDTOHelper;
import com.javaweb.service.IFundingIntervalDataService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;

@Service
public class FundingIntervalDataService implements IFundingIntervalDataService {

    public Map<String, FundingIntervalDTO> processFundingIntervalData(JsonNode fundingIntervalData) {
        Map<String, FundingIntervalDTO> fundingDataMap = new HashMap<>();

        if (fundingIntervalData != null) {
            String symbol = fundingIntervalData.get("symbol").asText();
            String adjustedFundingRateCap = fundingIntervalData.get("adjustedFundingRateCap").asText();
            String adjustedFundingRateFloor = fundingIntervalData.get("adjustedFundingRateFloor").asText();
            Long fundingIntervalHours = fundingIntervalData.get("fundingIntervalHours").asLong();

            FundingIntervalDTO fundingIntervalDTO = FundingIntervalDTOHelper.createFundingRateDTO(symbol,adjustedFundingRateCap,adjustedFundingRateFloor,fundingIntervalHours);

            fundingDataMap.put(symbol, fundingIntervalDTO);
        }

        return fundingDataMap;
    }
}
