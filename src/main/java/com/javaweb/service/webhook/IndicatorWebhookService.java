package com.javaweb.service.webhook;

import com.javaweb.dto.IndicatorDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.service.DateTimeHelper;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.model.trigger.IndicatorTrigger;
import com.javaweb.repository.UserRepository;
import com.javaweb.repository.trigger.IndicatorTriggerRepository;
import com.javaweb.service.impl.IndicatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IndicatorWebhookService {

    private final TelegramNotificationService telegramNotificationService;
    private final IndicatorTriggerRepository indicatorTriggerRepository;
    private final UserRepository userRepository;
    private final IndicatorService indicatorService;

    public void processIndicatorNotification(String symbol, String username) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Map<String, Double> indicatorDataMap = indicatorService.getIndicatorDataTriggers();

            IndicatorTrigger trigger = indicatorTriggerRepository.findBySymbolAndUsername(symbol, username);
            if (trigger != null) {
                String indicator = trigger.getIndicator();
                double value = trigger.getValue();
                String condition = trigger.getCondition();
                String indicatorCondition;
                if (indicator.equals("BOLL")) {
                    if (condition.equals(">=")) indicatorCondition = "Upper";
                    else indicatorCondition = "Lower";
                }
                else indicatorCondition = "";
                int period = trigger.getPeriod();
                String triggerType = "Indicator";
                double currentValue = indicatorDataMap.get("Indicator: " + symbol + " " + indicator + indicatorCondition + " " + period);

                Optional<userData> userDataOptional = Optional.ofNullable(userRepository.findByUsername(username));
                if (userDataOptional.isPresent()) {
                    userData user = userDataOptional.get();

                    String chat_id = null;
                    Optional<String> optionalChatId = Optional.ofNullable(user.getTelegram_id());
                    if (optionalChatId.isPresent()) {
                        chat_id = optionalChatId.get();
                    }

                    Map<String, Object> payload = new HashMap<>();
                    payload.put("symbol", symbol);
                    payload.put("condition", condition);
                    payload.put("chatID", "6870230085");
                    payload.put("timestamp", timestamp);
                    payload.put("indicator", indicator);
                    payload.put("indicatorValue", currentValue);
                    payload.put("value", value);
                    payload.put("period", period);
                    payload.put("triggerType", triggerType);

                    telegramNotificationService.sendNotification(payload);
                    System.out.println("Indicator Trigger notification sent for symbol: " + symbol + " with indicator: " + indicator);
                }
            } else {
                System.out.println("No IndicatorTrigger found for symbol: " + symbol);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Invalid indicator format for symbol: " + symbol);
        }
    }
}
