package com.javaweb.converter;

import com.javaweb.dto.IndicatorDTO;

import java.util.Map;

public class IndicatorDTOHelper {
    public static IndicatorDTO createIndicatorDTO(String symbol, Map<String, Object> values, String eventTime) {
        return new IndicatorDTO.Builder()
                .symbol(symbol)
                .values(values)
                .eventTime(eventTime)
                .build();
    }
}
