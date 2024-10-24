package com.javaweb.scheduler;

import com.javaweb.model.trigger.FundingRateInterval;
import com.javaweb.service.trigger.FundingRateIntervalService;
import com.javaweb.service.webhook.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FundingRateIntervalScheduler {

    @Autowired
    private FundingRateIntervalService fundingRateIntervalService;

    @Autowired
    private TelegramNotificationService telegramNotificationService;

    @Scheduled(cron = "0 0 * * * ?")  // Mỗi 1 giờ
    public void checkFundingRateIntervals() {
        // Giả sử userId là người dùng hiện tại
        String userId = "someUserId"; 

        // Lấy danh sách các cặp giao dịch do người dùng chọn
        List<String> tradingPairs = fundingRateIntervalService.getUserSelectedTradingPairs(userId);

        for (String symbol : tradingPairs) {
            FundingRateInterval newInterval = fundingRateIntervalService.fetchLatestFundingRateInterval(symbol);
            if (newInterval != null && fundingRateIntervalService.hasFundingRateIntervalChanged(newInterval)) {
                fundingRateIntervalService.saveFundingRateInterval(newInterval);

                // Gửi thông báo qua Telegram
                String message = "Funding rate interval của " + symbol + " đã thay đổi: " + newInterval.getIntervalTime();
                telegramNotificationService.sendTriggerNotification(message);

                System.out.println("Thông báo: " + message);
            }
        }
    }
}
