package com.javaweb.helpers.service;

import com.javaweb.dto.PriceDTO;

public class PriceDTOHelper {
    public static PriceDTO createPriceDTO(String symbol, String price, String eventTime) {
        return new PriceDTO.Builder()
                .symbol(symbol)
                .eventTime(eventTime)
                .price(price)
                .build();
    }
}
