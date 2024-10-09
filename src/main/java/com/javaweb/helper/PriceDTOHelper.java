package com.javaweb.helper;

import com.javaweb.DTO.PriceDTO;

public class PriceDTOHelper {

    // Hàm để tạo đối tượng PriceDTO từ các tham số đầu vào
    public static PriceDTO createPriceDTO(Long eventTime, String symbol, String price) {
        return new PriceDTO.Builder()
                .time(eventTime)
                .symbol(symbol)
                .price(price)
                .build();
    }
}
