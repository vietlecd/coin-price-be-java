package com.javaweb.helpers.Service;

import com.javaweb.DTO.PriceDTO;

public class PriceDTOHelper {

    // Hàm để tạo đối tượng PriceDTO từ các tham số đầu vào
    public static PriceDTO createPriceDTO(String eventTime, String symbol, String price) {
        return new PriceDTO.Builder()
                .eventTime(eventTime)
                .symbol(symbol)
                .price(price)
                .build();
    }
}
