package com.javaweb.service;

import com.javaweb.model.PriceDTO;

import java.util.Map;

public interface PriceDataService {
    void updatePriceData(String time, String symbol, String price);

    Map<String, PriceDTO> getPriceDataMap();
}
