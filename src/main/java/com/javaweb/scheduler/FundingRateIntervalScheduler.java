package com.javaweb.scheduler;

import com.javaweb.model.trigger.FundingRateInterval;
import com.javaweb.service.trigger.FundingRateIntervalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FundingRateIntervalScheduler {

    @Autowired
    private FundingRateIntervalService fundingRateIntervalService;

    @Scheduled(cron = "0 0 * * * ?")  
    public void checkFundingRateIntervals() {
        // Các cặp giao dịch chính
        String[] tradingPairs = {"BTCUSDT", "ETHUSDT", "BNBUSDT", "XRPUSDT", "ADAUSDT"};

        for (String symbol : tradingPairs) {
            FundingRateInterval newInterval = fundingRateIntervalService.fetchLatestFundingRateInterval(symbol);
            if (newInterval != null && fundingRateIntervalService.hasFundingRateIntervalChanged(newInterval)) {
                
                fundingRateIntervalService.saveFundingRateInterval(newInterval);
                System.out.println("Funding rate interval của " + symbol + " đã thay đổi: " + newInterval.getIntervalTime()); // Sửa đổi ở đây
            }
        }
    }
}
