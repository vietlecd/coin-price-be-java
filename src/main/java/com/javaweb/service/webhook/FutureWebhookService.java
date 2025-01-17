package com.javaweb.service.webhook;

import com.javaweb.dto.PriceDTO;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.model.trigger.FuturePriceTrigger;
import com.javaweb.repository.UserRepository;
import com.javaweb.repository.trigger.FuturePriceTriggerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FutureWebhookService {

    private final TelegramNotificationService telegramNotificationService;
    private final FuturePriceTriggerRepository futurePriceTriggerRepository;
    private final UserRepository userRepository;

    public void processFutureNotification(String symbol, PriceDTO futurePriceDTO, String username) {
        try {
            double futurePrice = Double.parseDouble(futurePriceDTO.getPrice());
            String timestamp = futurePriceDTO.getEventTime();

            FuturePriceTrigger trigger = futurePriceTriggerRepository.findBySymbolAndUsername(symbol, username);
            if (trigger != null) {
                double threshold = trigger.getFuturePriceThreshold();
                String condition = trigger.getCondition();
                String triggerType = "Future";

                Optional<userData> userDataOptional = Optional.ofNullable(userRepository.findByUsername(username));
                if (userDataOptional.isPresent()) {
                    userData user = userDataOptional.get();
                    Integer vip_role = user.getVip_role();

                    String chat_id = null;
                    Optional<String> optionalChatId = Optional.ofNullable(user.getTelegram_id());
                    if (optionalChatId.isPresent()) {
                        chat_id = optionalChatId.get();
                    }

                    Map<String, Object> payload = new HashMap<>();
                    payload.put("triggerType", triggerType);
                    payload.put("symbol", symbol);
                    payload.put("future_price", futurePrice);
                    payload.put("threshold", threshold);
                    payload.put("condition", condition);
                    payload.put("vip_role", vip_role);
                    payload.put("chatID", "5655972163");
                    payload.put("timestamp", timestamp);

                    telegramNotificationService.sendNotification(payload);
                    System.out.println("Future Trigger notification sent for symbol: " + symbol + " with threshold: " + threshold);
                }
            } else {
                System.out.println("No FuturePriceTrigger found for symbol: " + symbol);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Invalid price format for symbol: " + symbol);
        }
    }
}
