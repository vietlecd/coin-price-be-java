package com.javaweb.dto.telegram;

import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TelegramNotificationDTO {
    private String triggerType;
    private String symbol;
    private double spotPrice;
    private double futurePrice;
    private double fundingRate;
    private double threshold;
    private String condition;
    private Integer vip_role;
    private String chatId;
    private String timestamp;
}
