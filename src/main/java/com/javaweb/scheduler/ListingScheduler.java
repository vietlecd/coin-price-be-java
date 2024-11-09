package com.javaweb.scheduler;

import com.javaweb.dto.trigger.ListingDTO;
import com.javaweb.model.mongo_entity.ListingEntity;
import com.javaweb.service.trigger.CRUD.ListingTriggerService;
import com.javaweb.service.webhook.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ListingScheduler {

    @Autowired
    private ListingTriggerService listingService;

    @Autowired
    private TelegramNotificationService telegramNotificationService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void checkForNewListings() {

        if (!listingService.isNotificationAllowed()) {
            System.out.println("Thông báo hiện đang bị tắt.");
            return;
        }

        List<String> newSymbols = listingService.fetchNewListings();

        for (String symbol : newSymbols) {

            if (!listingService.existsBySymbol(symbol)) {

                ListingEntity listingEntity = new ListingEntity(symbol, "telegram", false); // Không kích hoạt trigger
                listingService.saveListing(listingEntity);

                Map<String, Object> payload = new HashMap<>();
                payload.put("message", "New symbol detected: " + symbol);
                telegramNotificationService.sendNotification(payload);
                System.out.println("New symbol saved: " + symbol + ". Notification sent via Telegram.");
            } else {
                System.out.println("Symbol already exists: " + symbol);
            }
        }

        if (newSymbols.isEmpty()) {
            System.out.println("No new trading pairs found.");
        }
    }
}
