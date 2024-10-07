package com.javaweb.helper;

import com.javaweb.model.PriceDTO;

public class PriceDTOHelper {

    // Hàm để tạo đối tượng PriceDTO từ các tham số đầu vào
    public static PriceDTO createPriceDTO(String time, String symbol, String price) {
        return new PriceDTO.Builder()
                .time(time)
                .symbol(symbol)
                .price(price)
                .build();
    }
}
