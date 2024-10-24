package com.javaweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.IndicatorDTO;

import java.util.List;
import java.util.Map;

public interface IIndicatorService {
    Map<String, IndicatorDTO> getIndicatorData(List<String> symbols, List<String> indicators, int days);
    public void handleIndicatorWebSocketMessage(JsonNode data, boolean isTriggered);
    public Map<String, IndicatorDTO> getIndicatorDataUsers();
    public Map<String, IndicatorDTO> getIndicatorDataTriggers();
}
