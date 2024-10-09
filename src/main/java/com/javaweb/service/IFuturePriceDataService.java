package com.javaweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.DTO.PriceDTO;

import java.util.Map;

public interface IFuturePriceDataService {

    void handleFutureWebSocketMessage(JsonNode data);

    Map<String, PriceDTO> getFuturePriceDataMap();
}