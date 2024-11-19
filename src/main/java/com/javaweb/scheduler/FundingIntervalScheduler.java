package com.javaweb.scheduler;

import com.javaweb.model.trigger.FundingInterval;
import com.javaweb.repository.trigger.FundingIntervalRepository;
import com.javaweb.service.webhook.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class FundingIntervalScheduler {

    @Autowired
    private FundingIntervalRepository fundingIntervalRepository;

    @Autowired
    private TelegramNotificationService telegramNotificationService;

    private final RestTemplate restTemplate = new RestTemplate();

    // Scheduled task to check funding interval and send notifications every hour
    @Scheduled(cron = "0 * * * * ?") // Runs every hour
    public void checkFundingIntervalChanges() {
        List<FundingInterval> fundingIntervals = fundingIntervalRepository.findAll();

        for (FundingInterval fundingInterval : fundingIntervals) {
            // Call Binance API to get the current funding info for the symbol
            Long newFundingIntervalHours = getFundingIntervalFromBinance(fundingInterval.getSymbol());

            // If there is any change in fundingIntervalHours, notify via Telegram
            if (newFundingIntervalHours != null && !newFundingIntervalHours.equals(fundingInterval.getFundingIntervalHours())) {
                // Create a map for the message payload
                Map<String, Object> payload = new HashMap<>();
                payload.put("message", "Funding interval for symbol " + fundingInterval.getSymbol() + " has changed to " + newFundingIntervalHours + " hours.");

                // Send notification to Telegram
                telegramNotificationService.sendNotification(payload);

                // Update the fundingIntervalHours in the database
                fundingInterval.setFundingIntervalHours(newFundingIntervalHours);
                fundingIntervalRepository.save(fundingInterval);
            }
        }
    }

    // Method to get funding interval from Binance API
    private Long getFundingIntervalFromBinance(String symbol) {
        String url = "https://api.binance.com/fapi/v1/fundingInfo?symbol=" + symbol;

        try {
            // Make the API request to Binance
            ResponseEntity<Map[]> response = restTemplate.getForEntity(url, Map[].class);

            // Parse the response and get the fundingIntervalHours
            if (response.getBody() != null && response.getBody().length > 0) {
                Map<String, Object> fundingInfo = response.getBody()[0];
                return ((Number) fundingInfo.get("fundingIntervalHours")).longValue();
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Log error or handle appropriately if the API call fails
        }

        // Return null or a default value if the API call fails or data is not found
        return null;
    }
}
