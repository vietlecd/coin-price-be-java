package com.javaweb.converter;


import com.javaweb.dto.telegram.TelegramNotificationDTO;

public class TelegramNotificationHelper {
    public static TelegramNotificationDTO createTelegramNotificationDTO ( String symbol, double price, double threshold, String condition, Integer vipRole, Integer chatId, String timestamp ) {
        return TelegramNotificationDTO.builder()
                .symbol(symbol)
                .price(price)
                .threshold(threshold)
                .condition(condition)
                .vip_role(vipRole)
                .chatId(chatId)
                .timestamp(timestamp)
                .build();
    }

}
