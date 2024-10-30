package com.javaweb.service.trigger;

import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.PriceDTO;

import com.javaweb.helpers.trigger.SnoozeCheckHelper;

import com.javaweb.helpers.trigger.TriggerCheckHelper;
import com.javaweb.repository.trigger.SpotPriceTriggerRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.impl.FundingRateDataService;
import com.javaweb.service.impl.FuturePriceDataService;
import com.javaweb.service.impl.SpotPriceDataService;
import com.javaweb.service.webhook.TelegramNotificationService;
import lombok.AllArgsConstructor;
import com.javaweb.service.webhook.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

@Service
@AllArgsConstructor
public class TriggerService {
    private SnoozeCheckHelper snoozeCheckHelper;
    private TriggerCheckHelper triggerCheckHelper;
    private FundingRateDataService fundingRateDataService;
    private SpotPriceDataService spotPriceDataService;
    private FuturePriceDataService futurePriceDataService;
    private FundingRateWebhookService fundingRateWebhookService;
    private SpotWebhookService spotWebhookService;
    private FutureWebhookService futureWebhookService;
    private PriceDifferenceWebhookService priceDifferenceWebhookService;
    private TelegramNotificationService telegramNotificationService;
    private SpotPriceTriggerRepository spotPriceTriggerRepository;
    private UserRepository userRepository;


    public void handleAndSendAlertForFundingRate(List<String> symbols, String username) {
        Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataTriggers();

        List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, fundingRateDataMap, "FundingRate" , username);
        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"Funding-rate",username);
        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {
                if (snoozeActive) {
                    System.out.println("Snooze is active, not sending alert for symbol: " + symbol);
                } else {
                    System.out.println("FundingRate Trigger fired for symbol: " + symbol);

                    FundingRateDTO fundingRateDTO = fundingRateDataMap.get("FundingRate Price: " + symbol);
                    if (fundingRateDTO != null ) {
                        fundingRateWebhookService.processFundingRateNotification(symbol, fundingRateDTO, username);
                    }

                }

            }
        }
    }
    public void handleAndSendAlertForPriceDifference(List<String> symbols, String username) {
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
                    System.out.println("PriceDifference Trigger fired for symbol: " + symbol);

                    PriceDTO spotPriceDTO = spotPriceDataMap.get("Spot Price: " + symbol);
                    PriceDTO futurePriceDTO = futurePriceDataMap.get("Future Price: " + symbol);
                    if (spotPriceDTO != null) {
                        priceDifferenceWebhookService.processPriceDifferenceNotification(symbol, spotPriceDTO, futurePriceDTO, username);
                    }
                    else {
                        System.out.println("Co so nao dau ma lam");
                    }
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
                    PriceDTO spotPriceDTO = priceDataMap.get("Spot Price: " + symbol);
                    if (spotPriceDTO != null) {
                        spotWebhookService.processSpotNotification(symbol, spotPriceDTO, username);
                    }
                    else {
                        System.out.println("Co so nao dau ma lam");
                    }
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
                    System.out.println("Future Trigger fired for symbol: " + symbol);

                    PriceDTO futurePriceDTO = priceDataMap.get("Future Price: " + symbol);
                    if (futurePriceDTO != null ) {
                        futureWebhookService.processFutureNotification(symbol, futurePriceDTO, username);
                    }
                    else {
                        System.out.println("Co so nao dau ma lam");
                    }
                }


            }
        }
    }

    public void handleAndSendAlertForIndicator(List<String> symbols, String username) {
        List<String> firedSymbols = triggerCheckHelper.checkIndicatorSymbolsAndTriggerAlert(symbols, username);
        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"Indicator",username);
        if (!firedSymbols.isEmpty()) {
            for (String symbol : firedSymbols) {
                if (snoozeActive) {
                    System.out.println("Indicator is active, not sending alert for symbol: " + symbol);
                } else {
                    // Gửi thông báo qua Telegram
                    //telegramNotificationService.sendTriggerNotification("Indicator Trigger fired for symbol: " + symbol + " with username: " + username);
                    System.out.println("Indicator Trigger fired for symbol: " + symbol);
                }

            }
        }
    }

}

