package com.javaweb.service.trigger;

import com.javaweb.converter.TelegramNotificationHelper;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.dto.IndicatorDTO;
import com.javaweb.dto.PriceDTO;

import com.javaweb.dto.telegram.TelegramNotificationDTO;
import com.javaweb.helpers.trigger.SnoozeCheckHelper;

import com.javaweb.helpers.trigger.TriggerCheckHelper;
import com.javaweb.model.mongo_entity.userData;
import com.javaweb.model.trigger.SpotPriceTrigger;
import com.javaweb.repository.SpotPriceTriggerRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.impl.FundingRateDataService;
import com.javaweb.service.impl.FuturePriceDataService;
import com.javaweb.service.impl.IndicatorService;
import com.javaweb.service.impl.SpotPriceDataService;
import com.javaweb.service.webhook.TelegramNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Math.abs;

@Service
@AllArgsConstructor
public class TriggerService {
    private SnoozeCheckHelper snoozeCheckHelper;
    private TriggerCheckHelper triggerCheckHelper;
    private FundingRateDataService fundingRateDataService;
    private SpotPriceDataService spotPriceDataService;
    private FuturePriceDataService futurePriceDataService;
    private TelegramNotificationService telegramNotificationService;
    private SpotPriceTriggerRepository spotPriceTriggerRepository;
    private UserRepository userRepository;

//    public void handleAndSendAlertForFundingRate(List<String> symbols, String username) {
//        Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataTriggers();
//
//        List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, fundingRateDataMap, "FundingRate" , username);
//        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"Funding-rate",username);
//        if (!firedSymbols.isEmpty()) {
//            for (String symbol : firedSymbols) {
//                if (snoozeActive) {
//                    System.out.println("Snooze is active, not sending alert for symbol: " + symbol);
//                } else {
//                    telegramNotificationService.sendTriggerNotification("FundingRate Trigger fired for symbol: " + symbol + " with username: " + username);
//                    System.out.println("FundingRate Trigger fired for symbol: " + symbol);
//                }
//
//            }
//        }
//    }
//    public void handleAndSendAlertForPriceDifference(List<String> symbols, String username) {
//        Map<String, PriceDTO> spotPriceDataMap = spotPriceDataService.getPriceDataTriggers();
//        Map<String, PriceDTO> futurePriceDataMap = futurePriceDataService.getPriceDataTriggers();
//
//        List<String> firedSymbols = triggerCheckHelper.checkCompareSymbolndTriggerAlert(symbols, spotPriceDataMap, futurePriceDataMap, username);
//        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"PriceDifference",username);
//        if (!firedSymbols.isEmpty()) {
//            for (String symbol : firedSymbols) {
//
//                if (snoozeActive ) {
//                    System.out.println("Snooze is active, not sending alert for symbol: " + symbol);
//                } else {
//                    // Gửi thông báo qua Telegram
//                    telegramNotificationService.sendTriggerNotification("Price Difference Trigger fired for symbol: " + symbol + " with username: " + username);
//                    System.out.println("PriceDifference Trigger fired for symbol: " + symbol);
//                }
//
//            }
//        }
//    }
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
                        try {
                            double price = Double.parseDouble(spotPriceDTO.getPrice());
                            String timestamp = spotPriceDTO.getEventTime();

                            SpotPriceTrigger trigger = spotPriceTriggerRepository.findBySymbolAndUsername(symbol, username);
                            if (trigger != null) {
                                double threshold = trigger.getSpotPriceThreshold();
                                String condition = trigger.getCondition();

                                Optional<userData> userDataOptional = Optional.ofNullable(userRepository.findByUsername(username));
                                if (userDataOptional.isPresent()) {
                                    userData user = userDataOptional.get();
                                    Integer vip_role = user.getVip_role();
                                    String chat_id = null;
                                    Optional<String> optionalChatId = Optional.ofNullable(user.getTelegram_id());

                                    if (optionalChatId.isPresent()) {
                                        chat_id = optionalChatId.get();
                                    }


                                    TelegramNotificationDTO notificationDTO = TelegramNotificationHelper.createTelegramNotificationDTO(
                                            symbol,
                                            price,
                                            threshold,
                                            condition,
                                            vip_role,
                                            chat_id,
                                            timestamp
                                    );
                                    telegramNotificationService.sendTriggerNotification(notificationDTO);
                                    System.out.println("Spot Trigger notification sent for symbol: " + symbol + " with threshold: " + threshold);
                                }
                            } else {
                                System.out.println("No SpotPriceTrigger found for symbol: " + symbol);
                            }
                        } catch(NumberFormatException e) {
                            e.printStackTrace();
                            System.out.println("sai so roi nha");
                        }
                    }
                    else {
                        System.out.println("Co so nao dau ma lam");
                    }

                }
            }
        }
    }

//    public void handleAndSendAlertForFuture(List<String> symbols, String username) {
//        Map<String, PriceDTO> priceDataMap = futurePriceDataService.getPriceDataTriggers();
//        List<String> firedSymbols = triggerCheckHelper.checkSymbolAndTriggerAlert(symbols, priceDataMap, "Future", username);
//        boolean snoozeActive = snoozeCheckHelper.checkSymbolAndSnooze(symbols,"Future",username);
//        if (!firedSymbols.isEmpty()) {
//            for (String symbol : firedSymbols) {
//                if (snoozeActive) {
//                    System.out.println("Future is active, not sending alert for symbol: " + symbol);
//                } else {
//                    // Gửi thông báo qua Telegram
//                    telegramNotificationService.sendTriggerNotification("Future Trigger fired for symbol: " + symbol + " with username: " + username);
//                    System.out.println("Future Trigger fired for symbol: " + symbol);
//                }
//            }
//        }
//    }

}

