package com.javaweb.service.webhook;

import com.javaweb.dto.PriceDTO;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.model.trigger.SpotPriceTrigger;
import com.javaweb.repository.UserRepository;
import com.javaweb.repository.trigger.SpotPriceTriggerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SpotWebhookService {

    private final TelegramNotificationService telegramNotificationService;
    private final SpotPriceTriggerRepository spotPriceTriggerRepository;
    private final UserRepository userRepository;

    public void processSpotNotification(String symbol, PriceDTO spotPriceDTO, String username) {
        try {
            double spotPrice = Double.parseDouble(spotPriceDTO.getPrice());
            String timestamp = spotPriceDTO.getEventTime();

            SpotPriceTrigger trigger = spotPriceTriggerRepository.findBySymbolAndUsername(symbol, username);
            if (trigger != null) {
                double threshold = trigger.getSpotPriceThreshold();
                String condition = trigger.getCondition();
                String triggerType = "spot";

                Optional<userData> userDataOptional = Optional.ofNullable(userRepository.findByUsername(username));
                if (userDataOptional.isPresent()) {
                    userData user = userDataOptional.get();

                    String chat_id = null;
                    Optional<String> optionalChatId = Optional.ofNullable(user.getTelegram_id());


                    Map<String, Object> payload = new HashMap<>();
                    payload.put("symbol", "BTC");
                    payload.put("spot_price", spotPrice);
                    payload.put("threshold", threshold);
                    payload.put("condition", condition);
                    payload.put("chatID", "5655972163");
                    payload.put("timestamp", timestamp);
                    payload.put("triggerType", triggerType);


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
