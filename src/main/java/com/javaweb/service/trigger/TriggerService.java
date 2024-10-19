package com.javaweb.service.trigger;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.connect.impl.FundingIntervalWebService;
import com.javaweb.connect.impl.FundingRateWebSocketService;
import com.javaweb.connect.impl.FutureWebSocketService;
import com.javaweb.connect.impl.SpotWebSocketService;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.PriceDTO;
import com.javaweb.helpers.controller.FundingRateAndIntervalHelper;
import com.javaweb.helpers.sse.SseHelper;
import com.javaweb.helpers.trigger.SnoozeCheckHelper;
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
    private SseHelper sseHelper;
    @Autowired
    private SnoozeCheckHelper snoozeCheckHelper;
    @Autowired
    private TriggerCheckHelper triggerCheckHelper;

    @Autowired
    private FundingRateWebSocketService fundingRateWebSocketService;

    @Autowired
    private FundingRateDataService fundingRateDataService;

    @Autowired
    private FundingIntervalWebService fundingIntervalWebService;

    @Autowired
    private SpotPriceDataService spotPriceDataService;

    @Autowired
    private FuturePriceDataService futurePriceDataService;

    @Autowired
    private TelegramNotificationService telegramNotificationService;

    public void handleAndSendAlertForFundingRate(List<String> symbols, String username) {
        Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataMap();

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
        Map<String, PriceDTO> spotPriceDataMap = spotPriceDataService.getPriceDataMap();
        Map<String, PriceDTO> futurePriceDataMap = futurePriceDataService.getPriceDataMap();

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
        Map<String, PriceDTO> priceDataMap = spotPriceDataService.getPriceDataMap();
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
        Map<String, PriceDTO> priceDataMap = futurePriceDataService.getPriceDataMap();
        List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, priceDataMap, "Future", username);
        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"Spot",username);
        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {


                if (snoozeActive ) {
                    System.out.println("Future is active, not sending alert for symbol: " + symbol);
                } else {
                    System.out.println("Future Trigger fired for symbol: " + symbol);
                // Gửi thông báo qua Telegram
                telegramNotificationService.sendTriggerNotification("Future Trigger fired for symbol: " + symbol + " with username: " + username);
            }
        }
    }

    public SseEmitter handleStreamFundingRate(List<String> symbols, String username, WebSocketConfig webSocketConfig) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        //ham update data interval sau 15 min
        scheduleFundingIntervalDataUpdate(symbols);

        fundingRateWebSocketService.connectToWebSocket(symbols);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataMap();
            List<Map<String, FundingIntervalDTO>> fundingIntervalDataList = fundingIntervalWebService.getLatestFundingIntervalData(symbols);

            FundingRateAndIntervalHelper.streamCombinedData(sseEmitter, symbols, fundingRateDataMap, fundingIntervalDataList);

        }, 0, 5, TimeUnit.SECONDS); //trigger mỗi 5s

        return sseEmitter;
    }

    public void scheduleFundingIntervalDataUpdate(List<String> symbols) {
        ScheduledExecutorService dataUpdater = Executors.newScheduledThreadPool(1);
        dataUpdater.scheduleAtFixedRate(() -> {
            fundingIntervalWebService.getLatestFundingIntervalData(symbols);
            System.out.println("FundingInterval data updated for symbols: " + symbols);
        }, 0, 15, TimeUnit.MINUTES);
    }
}
