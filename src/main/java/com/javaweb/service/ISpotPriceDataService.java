package com.javaweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.dto.PriceDTO;

import java.util.Map;

public interface ISpotPriceDataService {

    void handleSpotWebSocketMessage(JsonNode data);

    Map<String, PriceDTO> getSpotPriceDataMap();
}
