package com.javaweb.dto.telegram;

import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TelegramNotificationDTO {
    private String symbol;
    private double price;
    private double threshold;
    private String condition;
    private Integer vip_role;
    private Integer chatId;
    private String timestamp;
}
