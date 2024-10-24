package com.javaweb.scheduler;

import com.javaweb.dto.FundingIntervalDTO;
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

    @Scheduled(cron = "0 0 * * * ?")  // Every hour
    public void checkFundingRateIntervals() {
        // Assume userId is the current user
        String userId = "someUserId"; 

        // Get the list of trading pairs selected by the user
        List<String> tradingPairs = fundingRateIntervalService.getUserSelectedTradingPairs(userId);

        for (String symbol : tradingPairs) {
            FundingIntervalDTO newInterval = fundingRateIntervalService.fetchLatestFundingRateInterval(symbol);
            if (newInterval != null && fundingRateIntervalService.hasFundingRateIntervalChanged(newInterval)) {
                fundingRateIntervalService.saveFundingRateInterval(newInterval);

                // Send notification via Telegram
                String message = "Funding rate interval of " + symbol + " has changed: " + newInterval.getFundingIntervalHours();
                telegramNotificationService.sendTriggerNotification(message);

                System.out.println("Notification: " + message);
            }
        }
    }
}
