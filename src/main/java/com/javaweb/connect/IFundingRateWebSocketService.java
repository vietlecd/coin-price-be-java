package com.javaweb.connect;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.DTO.FundingIntervalDTO;
import com.javaweb.DTO.FundingRateDTO;
import com.javaweb.DTO.PriceDTO;

import java.util.List;
import java.util.Map;

public interface IFundingRateWebSocketService {
    // Connect to WebSocket for Funding Rate
    void connectToFundingRateWebSocket(List<String> streams);

    void closeWebSocket();



    interface IFundingDataService {
        void updateFundingRate(FundingRateDTO fundingRateDTO);

        void updateFundingInterval(FundingIntervalDTO fundingIntervalDTO);

        Map<String, FundingRateDTO> getFundingRateDataMap();

        Map<String, FundingIntervalDTO> getFundingIntervalDataMap();
    }
}
