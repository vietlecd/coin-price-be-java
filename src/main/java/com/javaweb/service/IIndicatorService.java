package com.javaweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.IndicatorDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public interface IIndicatorService {
    Map<String, IndicatorDTO> getIndicatorData(List<String> symbols, List<String> indicators, int days, String username) throws TimeoutException;
    public void handleComparedIndicatorValue(String symbol, String indicator, int days, double value);
    public Map<String, Double> getIndicatorDataTriggers();
}
