package com.javaweb.service.trigger;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.impl.FundingIntervalWebService;
import com.javaweb.connect.impl.FundingRateWebSocketService;
import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.trigger.TriggerCheckHelper;
import com.javaweb.service.impl.FundingRateDataService;
import com.javaweb.service.impl.FuturePriceDataService;
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
    private TriggerCheckHelper triggerCheckHelper;

    @Autowired
    private FundingRateDataService fundingRateDataService;

    @Autowired
    private SpotPriceDataService spotPriceDataService;

    @Autowired
    private FuturePriceDataService futurePriceDataService;

    @Autowired
    private TelegramNotificationService telegramNotificationService;

    public void handleAndSendAlertForFundingRate(List<String> symbols, String username) {
        Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataTriggers();

        List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, fundingRateDataMap, "FundingRate" , username);

        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {
                System.out.println("Spot Trigger fired for symbol: " + symbol);
                // Gửi thông báo qua Telegram
                telegramNotificationService.sendTriggerNotification("FundingRate Trigger fired for symbol: " + symbol + " with username: " + username);
            }
        }
    }
    public void handleAndSendAlertForSpotAndFuture(List<String> symbols, String username) {
        Map<String, PriceDTO> spotPriceDataMap = spotPriceDataService.getPriceDataTriggers();
        Map<String, PriceDTO> futurePriceDataMap = futurePriceDataService.getPriceDataTriggers();

        List<String> firedSymbols = triggerCheckHelper.checkCompareSymbolndTriggerAlert(symbols, spotPriceDataMap, futurePriceDataMap, username);

        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {
                System.out.println("Spot Trigger fired for symbol: " + symbol);
                // Gửi thông báo qua Telegram
                telegramNotificationService.sendTriggerNotification("Price Difference Trigger fired for symbol: " + symbol + " with username: " + username);
            }
        }
    }
    public void handleAndSendAlertForSpot(List<String> symbols, String username) {
        Map<String, PriceDTO> priceDataMap = spotPriceDataService.getPriceDataTriggers();
        List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, priceDataMap, "Spot", username);

        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {
                System.out.println("Spot Trigger fired for symbol: " + symbol);
                // Gửi thông báo qua Telegram
                telegramNotificationService.sendTriggerNotification("Spot Trigger fired for symbol: " + symbol + " with username: " + username);
            }
        }
    }

    public void handleAndSendAlertForFuture(List<String> symbols, String username) {
        Map<String, PriceDTO> priceDataMap = futurePriceDataService.getPriceDataTriggers();
        List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, priceDataMap, "Future", username);

        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {
                System.out.println("Future Trigger fired for symbol: " + symbol);
                // Gửi thông báo qua Telegram
                telegramNotificationService.sendTriggerNotification("Future Trigger fired for symbol: " + symbol + " with username: " + username);
            }
        }
    }

}
