package com.javaweb.service.webhook;

import com.javaweb.dto.PriceDTO;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.model.trigger.PriceDifferenceTrigger;
import com.javaweb.repository.UserRepository;
import com.javaweb.repository.trigger.PriceDifferenceTriggerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PriceDifferenceWebhookService {

    private final TelegramNotificationService telegramNotificationService;
    private final PriceDifferenceTriggerRepository priceDifferenceTriggerRepository;
    private final UserRepository userRepository;

    public void processPriceDifferenceNotification(String symbol, PriceDTO spotPriceDTO, PriceDTO futurePriceDTO, String username) {
        try {
            double spotPrice = Double.parseDouble(spotPriceDTO.getPrice());
            double futurePrice = Double.parseDouble(futurePriceDTO.getPrice());
            String timestamp = spotPriceDTO.getEventTime();

            PriceDifferenceTrigger trigger = priceDifferenceTriggerRepository.findBySymbolAndUsername(symbol, username);
            if (trigger != null) {
                double threshold = trigger.getPriceDifferenceThreshold();
                String condition = trigger.getCondition();
                String triggerType = "Price Diff";

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
                    payload.put("spotPrice", spotPrice);
                    payload.put("futurePrice", futurePrice);
                    payload.put("threshold", threshold);
                    payload.put("condition", condition);
                    payload.put("chatID", chat_id);
                    payload.put("timestamp", timestamp);


                    telegramNotificationService.sendNotification(payload);
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
