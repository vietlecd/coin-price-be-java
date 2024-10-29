package com.javaweb.service.webhook;

import com.javaweb.dto.FundingRateDTO;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.repository.trigger.FundingRateTriggerRepository;
import com.javaweb.repository.trigger.FuturePriceTriggerRepository;
import com.javaweb.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FundingRateWebhookService {

    private final TelegramNotificationService telegramNotificationService;
    private final FundingRateTriggerRepository fundingRateTriggerRepository;
    private final UserRepository userRepository;

    public void processFundingRateNotification(String symbol, FundingRateDTO fundingRateDTO, String username) {
        try {
            double fundingRate = Double.parseDouble(fundingRateDTO.getFundingRate());
            String timestamp = fundingRateDTO.getEventTime();

            FundingRateTrigger trigger = fundingRateTriggerRepository.findBySymbolAndUsername(symbol, username);
            if (trigger != null) {
                double threshold = trigger.getFundingRateThreshold();
                String condition = trigger.getCondition();
                String triggerType = "FundingRate";

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
                    payload.put("fundingRate", fundingRate);
                    payload.put("threshold", threshold);
                    payload.put("condition", condition);
                    payload.put("vip_role", vip_role);
                    payload.put("chatID", chat_id);
                    payload.put("timestamp", timestamp);

                    telegramNotificationService.sendNotification(payload);
                    System.out.println("FundingRate Trigger notification sent for symbol: " + symbol + " with threshold: " + threshold);
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
