package com.javaweb.scheduler;

import com.javaweb.dto.trigger.ListingDTO;
import com.javaweb.repository.ListingRepository;
import com.javaweb.service.webhook.TelegramNotificationService;
import com.javaweb.service.trigger.CRUD.ListingTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ListingScheduler {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private TelegramNotificationService telegramNotificationService;

    @Autowired
    private ListingTriggerService listingTriggerService;

    private boolean hasNotifiedToday = false;


    @Scheduled(cron = "0 0 8 * * ?")
    public void checkForNewListings() {
        if (hasNotifiedToday) {
            System.out.println("Thông báo đã được gửi hôm nay.");
            return;
        }


        List<String> newSymbols = listingTriggerService.fetchNewListings();

        if (newSymbols.isEmpty()) {
            System.out.println("Không có symbol mới để thông báo.");
            return;
        }


        StringBuilder messageBuilder = new StringBuilder("Phát hiện các symbol mới: ");
        for (String symbol : newSymbols) {
            ListingDTO existingTrigger = listingRepository.findBySymbol(symbol);
            if (existingTrigger == null) {

                ListingDTO newTrigger = new ListingDTO.Builder()
                        .setSymbol(symbol)
                        .setNotificationMethod("telegram")
                        .setShouldNotify(true)
                        .build();

                listingRepository.save(newTrigger);
                messageBuilder.append(symbol).append(" ");
            }
        }

        if (messageBuilder.length() > 0) {

            Map<String, Object> payload = new HashMap<>();
            payload.put("message", messageBuilder.toString().trim());
            telegramNotificationService.sendNotification(payload);


            hasNotifiedToday = true;
            System.out.println("Đã gửi thông báo cho các symbol mới.");
        }
    }

    // Cron job để reset lại flag khi bắt đầu một ngày mới
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetNotificationFlag() {
        hasNotifiedToday = false;
        System.out.println("Flag thông báo đã được reset cho ngày mới.");
    }
}

