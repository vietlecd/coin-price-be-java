package com.javaweb.service.trigger;

import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.IndicatorDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.trigger.SnoozeCheckHelper;
import com.javaweb.helpers.trigger.TriggerCheckHelper;
import com.javaweb.service.impl.FundingRateDataService;
import com.javaweb.service.impl.FuturePriceDataService;
import com.javaweb.service.impl.IndicatorService;
import com.javaweb.service.impl.SpotPriceDataService;
import com.javaweb.service.webhook.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Math.abs;

@Service
public class TriggerService {
    @Autowired
    private SnoozeCheckHelper snoozeCheckHelper;

    @Autowired
    private TriggerCheckHelper triggerCheckHelper;

    @Autowired
    private FundingRateDataService fundingRateDataService;

    @Autowired
    private SpotPriceDataService spotPriceDataService;

    @Autowired
    private FuturePriceDataService futurePriceDataService;

    @Autowired
    private TelegramNotificationService telegramNotificationService;

    @Autowired
    private IndicatorService indicatorService;

    public void handleAndSendAlertForFundingRate(List<String> symbols, String username) {
        Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataTriggers();

        List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, fundingRateDataMap, "FundingRate" , username);
        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"Funding-rate",username);
        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {
                if (snoozeActive) {
                    System.out.println("Snooze is active, not sending alert for symbol: " + symbol);
                } else {
                    telegramNotificationService.sendTriggerNotification("FundingRate Trigger fired for symbol: " + symbol + " with username: " + username);
                    System.out.println("FundingRate Trigger fired for symbol: " + symbol);
                }

            }
        }
    }
    public void handleAndSendAlertForSpotAndFuture(List<String> symbols, String username) {
        Map<String, PriceDTO> spotPriceDataMap = spotPriceDataService.getPriceDataTriggers();
        Map<String, PriceDTO> futurePriceDataMap = futurePriceDataService.getPriceDataTriggers();

        List<String> firedSymbols = triggerCheckHelper.checkCompareSymbolndTriggerAlert(symbols, spotPriceDataMap, futurePriceDataMap, username);
        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"PriceDifference",username);
        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {

                if (snoozeActive ) {
                    System.out.println("Snooze is active, not sending alert for symbol: " + symbol);
                } else {
                    // Gửi thông báo qua Telegram
                    telegramNotificationService.sendTriggerNotification("Price Difference Trigger fired for symbol: " + symbol + " with username: " + username);
                    System.out.println("PriceDifference Trigger fired for symbol: " + symbol);
                }

            }
        }
    }
    public void handleAndSendAlertForSpot(List<String> symbols, String username) {
        Map<String, PriceDTO> priceDataMap = spotPriceDataService.getPriceDataTriggers();
        List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, priceDataMap, "Spot", username);
        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"Spot",username);
        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {

                if (snoozeActive ) {
                    System.out.println("Snooze is active, not sending alert for symbol: " + symbol);
                } else {
                    System.out.println("Spot Trigger fired for symbol: " + symbol);
                    // Gửi thông báo qua Telegram
                    telegramNotificationService.sendTriggerNotification("Spot Trigger fired for symbol: " + symbol + " with username: " + username);
                }
            }
        }
    }

    public void handleAndSendAlertForFuture(List<String> symbols, String username) {
        Map<String, PriceDTO> priceDataMap = futurePriceDataService.getPriceDataTriggers();
        List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, priceDataMap, "Future", username);
        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"Future",username);
        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {
                if (snoozeActive) {
                    System.out.println("Future is active, not sending alert for symbol: " + symbol);
                } else {
                    // Gửi thông báo qua Telegram
                    telegramNotificationService.sendTriggerNotification("Future Trigger fired for symbol: " + symbol + " with username: " + username);
                    System.out.println("Future Trigger fired for symbol: " + symbol);
                }

            }
        }
    }

    public void handleAndSendAlertForIndicator(List<String> symbols, String username) {
        Map<String, IndicatorDTO> indicatorDataMap = indicatorService.getIndicatorDataTriggers();
        List<String> firedSymbols = triggerCheckHelper.checkIndicatorSymbolsAndTriggerAlert(symbols, indicatorDataMap, username);
        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"Indicator",username);
        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {
                if (snoozeActive) {
                    System.out.println("Indicator is active, not sending alert for symbol: " + symbol);
                } else {
                    // Gửi thông báo qua Telegram
                    telegramNotificationService.sendTriggerNotification("Indicator Trigger fired for symbol: " + symbol + " with username: " + username);
                    System.out.println("Indicator Trigger fired for symbol: " + symbol);
                }

            }
        }
    }

}