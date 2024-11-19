package com.javaweb.scheduler;

import com.javaweb.model.trigger.DelistingEntity;
import com.javaweb.service.trigger.CRUD.DelistingTriggerService;
import com.javaweb.service.webhook.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DelistingScheduler {

    @Autowired
    private DelistingTriggerService delistingService;

    @Autowired
    private TelegramNotificationService telegramNotificationService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void checkForNewDelistings() {
        if (!delistingService.isNotificationAllowed()) {
            System.out.println("Thông báo hiện đang bị tắt.");
            return;
        }

        //delistingService.deleteAllDelistings();
        //System.out.println("All delisting records have been deleted. Starting fresh scrape.");


        // Lấy danh sách tiêu đề mới
        List<String> newTitles = delistingService.fetchNewDelistings();

        for (String title : newTitles) {

            if (!delistingService.existsByTitle(title)) {

                DelistingEntity delistingEntity = new DelistingEntity(title, "telegram", false);
                delistingService.saveDelisting(delistingEntity);

                // Tạo payload cho thông báo
                Map<String, Object> payload = new HashMap<>();
                payload.put("triggerType", "Delisting");
                payload.put("message",title);
                payload.put("chatID", "5655972163");

                // Gửi thông báo qua Telegram
                try {
                    telegramNotificationService.sendNotification(payload);
                    System.out.println("New delisting saved: " + title + ". Notification sent via Telegram.");
                } catch (Exception e) {
                    System.err.println("Failed to send notification for title: " + title);
                    e.printStackTrace();
                }
            } else {
                System.out.println("Delisting title already exists: " + title);
            }
        }

        if (newTitles.isEmpty()) {
            System.out.println("No new delisting titles found.");
        }
    }
}
