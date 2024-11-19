package com.javaweb.scheduler;

import com.javaweb.model.trigger.ListingEntity;
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

    @Scheduled(cron = "0 0 7 * * ?") // Adjust cron as needed
    public void checkForNewListings() {

        // Kiểm tra xem thông báo có được phép gửi không
        if (!listingService.isNotificationAllowed()) {
            System.out.println("Thông báo hiện đang bị tắt.");
            return;
        }

        //listingService.deleteAllListings();
        //System.out.println("All listing records have been deleted. Starting fresh scrape.");


        // Lấy danh sách các symbol mới từ service
        List<String> newSymbols = listingService.fetchNewListings();

        for (String symbol : newSymbols) {

            // Kiểm tra xem symbol đã có trong cơ sở dữ liệu chưa
            if (!listingService.existsBySymbol(symbol)) {

                // Lưu symbol mới vào cơ sở dữ liệu
                ListingEntity listingEntity = new ListingEntity(symbol, "telegram", false); // Không kích hoạt trigger ngay
                listingService.saveListing(listingEntity);

                // Tạo payload cho thông báo
                Map<String, Object> payload = new HashMap<>();
                payload.put("triggerType", "Listing");
                payload.put("message", "New symbol detected: " + symbol);
                payload.put("chatID", "5655972163");  // Chỉ định ID của nhóm hoặc người nhận thông báo

                // Gửi thông báo qua Telegram
                try {
                    telegramNotificationService.sendNotification(payload);
                    System.out.println("New symbol saved: " + symbol + ". Notification sent via Telegram.");
                } catch (Exception e) {
                    System.err.println("Failed to send notification for symbol: " + symbol);
                    e.printStackTrace();
                }
            } else {
                System.out.println("Symbol already exists: " + symbol);
            }
        }

        // Kiểm tra nếu không có symbol mới
        if (newSymbols.isEmpty()) {
            System.out.println("No new trading pairs found.");
        }
    }
}
