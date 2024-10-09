package com.javaweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.DTO.FundingRateDTO;

import java.util.Map;

public interface IFundingRateDataService {

    void handleFundingRateWebSocketMessage(JsonNode data);

    Map<String, FundingRateDTO> getFundingRateDataMap();
}
