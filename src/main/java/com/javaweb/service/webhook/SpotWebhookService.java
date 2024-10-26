package com.javaweb.service.webhook;

import com.javaweb.converter.TelegramNotificationHelper;
import com.javaweb.dto.PriceDTO;
import com.javaweb.dto.telegram.TelegramNotificationDTO;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.model.trigger.SpotPriceTrigger;
import com.javaweb.repository.SpotPriceTriggerRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.webhook.TelegramNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SpotWebhookService {

    private final TelegramNotificationService telegramNotificationService;
    private final SpotPriceTriggerRepository spotPriceTriggerRepository;
    private final UserRepository userRepository;

    public void processSpotNotification(String symbol, PriceDTO spotPriceDTO, String username) {
        try {
            double price = Double.parseDouble(spotPriceDTO.getPrice());
            String timestamp = spotPriceDTO.getEventTime();

            SpotPriceTrigger trigger = spotPriceTriggerRepository.findBySymbolAndUsername(symbol, username);
            if (trigger != null) {
                double threshold = trigger.getSpotPriceThreshold();
                String condition = trigger.getCondition();

                Optional<userData> userDataOptional = Optional.ofNullable(userRepository.findByUsername(username));
                if (userDataOptional.isPresent()) {
                    userData user = userDataOptional.get();
                    Integer vip_role = user.getVip_role();
                    String chat_id = null;
                    Optional<String> optionalChatId = Optional.ofNullable(user.getTelegram_id());
                    if (optionalChatId.isPresent()) {
                        chat_id = optionalChatId.get();
                    }

                    TelegramNotificationDTO notificationDTO = TelegramNotificationHelper.createTelegramNotificationDTO(
                            symbol,
                            spotPrice,
                            threshold,
                            condition,
                            vip_role,
                            chat_id,
                            timestamp
                    );

                    telegramNotificationService.sendTriggerNotification(notificationDTO);
                    System.out.println("Spot Trigger notification sent for symbol: " + symbol + " with threshold: " + threshold);
                }
            } else {
                System.out.println("No SpotPriceTrigger found for symbol: " + symbol);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Invalid price format for symbol: " + symbol);
        }
    }
}
