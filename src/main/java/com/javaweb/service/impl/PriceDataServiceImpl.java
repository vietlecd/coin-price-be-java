package com.javaweb.service.impl;

import com.javaweb.model.PriceDTO;
import com.javaweb.service.PriceDataService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceDataServiceImpl implements PriceDataService {
    private final Map<String, PriceDTO> priceDataMap = new ConcurrentHashMap<>();

    public void updatePriceData(String time, String symbol, String price) {
        priceDataMap.put(symbol, new PriceDTO(time, price));
    }

    public Map<String, PriceDTO> getPriceDataMap() {
        return priceDataMap;
    }
}
