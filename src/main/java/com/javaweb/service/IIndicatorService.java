package com.javaweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.IndicatorDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public interface IIndicatorService {
<<<<<<< HEAD
    Map<String, IndicatorDTO> getIndicatorData(List<String> symbols, List<String> indicators, int days, String username) throws TimeoutException;
    public void handleFundingRateWebSocketMessage(JsonNode data, boolean isTriggered);
    public Map<String, IndicatorDTO> getIndicatorDataTriggers();
=======

    Map<String, IndicatorDTO> getIndicatorData(List<String> symbols, List<String> indicators, int days, String username);
>>>>>>> parent of 206a011 (Merge pull request #45 from dath-241/developer)
}
