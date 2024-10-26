package com.javaweb.converter;


import com.javaweb.dto.telegram.TelegramNotificationDTO;

public class TelegramNotificationHelper {
    public static TelegramNotificationDTO createTelegramNotificationDTO ( String triggerType, String symbol, double spotPrice, double futurePrice, double fundingRate, double threshold, String condition, Integer vipRole, String chatId, String timestamp ) {
        return TelegramNotificationDTO.builder()
                .triggerType(triggerType)
                .symbol(symbol)
                .spotPrice(spotPrice)
                .futurePrice(futurePrice)
                .fundingRate(fundingRate)
                .threshold(threshold)
                .condition(condition)
                .vip_role(vipRole)
                .chatId(chatId)
                .timestamp(timestamp)
                .build();
    }

}
