package com.javaweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.PriceDTO;

import java.util.Map;

public interface IPriceDataService {

    void handleWebSocketMessage(JsonNode data);

    Map<String, PriceDTO> getPriceDataMap();
}