
package com.javaweb.helpers.trigger;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

import com.javaweb.dto.snooze.PriceDifferenceSnoozeCondition;
import com.javaweb.dto.snooze.SpotSnoozeCondition;
import com.javaweb.dto.snooze.FutureSnoozeCondition;
import com.javaweb.dto.snooze.FundingRateSnoozeCondition;
import com.javaweb.repository.PriceDifferenceSnoozeConditionRepository;
import com.javaweb.repository.SpotSnoozeConditionRepository;
import com.javaweb.repository.FutureSnoozeConditionRepository;
import com.javaweb.repository.FundingRateSnoozeConditionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SnoozeCheckHelper {

    @Autowired
    private SpotSnoozeConditionRepository spotSnoozeConditionRepository;

    @Autowired
    private FutureSnoozeConditionRepository futureSnoozeConditionRepository;

    @Autowired
    private PriceDifferenceSnoozeConditionRepository priceDifferenceSnoozeConditionRepository;
    @Autowired
    private FundingRateSnoozeConditionRepository fundingRateSnoozeConditionRepository;
    @Autowired
    private SpotSnoozeCondition spotSnoozeCondition;
    public boolean checkSymbolAndSnooze(List<String> symbols, String type,String username) {
        boolean anyConditionMet = false; // Khởi tạo trạng thái ban đầu là không có điều kiện nào thỏa mãn

        for (String symbol : symbols) {
            boolean conditionMet = false;

            // Kiểm tra loại snooze dựa trên type
            switch (type) {
                case "Spot":
                    conditionMet = checkSpotSnooze( symbol, username); // Kiểm tra snooze cho spot
                    break;
                case "Future":
                    conditionMet = checkFutureSnooze(symbol,username); // Kiểm tra snooze cho future
                    break;
                case "Funding-rate":
                    conditionMet = checkFundingRateSnooze(symbol,username); // Kiểm tra snooze cho funding-rate
                    break;
                case"PriceDifference":
                    conditionMet = checkPriceDifferenceSnooze(symbol,username);
                    default:
                    System.out.println("Unknown trigger type: " + type);
            }

            // Nếu có bất kỳ điều kiện snooze nào thỏa mãn, đánh dấu là true và không cần dừng vòng lặp
            if (conditionMet) {
                anyConditionMet = conditionMet; // Cập nhật trạng thái đã có điều kiện thỏa mãn
            }
        }

        // Trả về true nếu bất kỳ điều kiện snooze nào thỏa mãn
        return anyConditionMet;
    }
    public boolean checkSpotSnooze(String symbol,String username) {
        // Tìm SpotSnoozeCondition theo symbol

        Optional<SpotSnoozeCondition> spotSnoozeConditionOptional = spotSnoozeConditionRepository
                .findBySymbolAndUsername(symbol,username);
        boolean snoozeActive = false; // Biến cờ để theo dõi trạng thái snooze

        if (spotSnoozeConditionOptional.isPresent()) {
            SpotSnoozeCondition condition = spotSnoozeConditionOptional.get();
            LocalDateTime now = LocalDateTime.now();

            // Kiểm tra loại snooze và đánh giá điều kiện
            switch (condition.getSnoozeType()) {
                case "ONE_TIME":
                    // Kiểm tra nếu thời gian hiện tại là trước thời gian kết thúc
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("One-time snooze for symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                case "ONCE_IN_DURATION":
                    // Kiểm tra nếu thời gian hiện tại nằm trong khoảng start và end
                    if ( now.isBefore(condition.getEndTime())) {
                        System.out.println("Duration snooze for symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                case "SPECIFIC_TIME":
                    // Kiểm tra nếu thời gian hiện tại nằm trong phạm vi 30 phút của thời gian cụ thể
                    LocalDateTime specificTime = LocalDateTime.parse(condition.getSpecificTime());
                    if (now.isBefore(specificTime.plusMinutes(30)) && now.isAfter(specificTime.minusMinutes(30))) {
                        System.out.println("Specific time snooze for symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                default:
                    System.out.println("Unknown snooze type for symbol: " + symbol);
                    break;
            }
        }

        // Trả về trạng thái snooze (true nếu có snooze hoạt động, false nếu không)
        return snoozeActive;
    }

    public boolean checkFutureSnooze(String symbol, String username) {
        // Tìm FutureSnoozeCondition theo symbol
        Optional<FutureSnoozeCondition> futureSnoozeConditionOptional = futureSnoozeConditionRepository
                .findBySymbolAndUsername(symbol, username);
        boolean snoozeActive = false; // Biến cờ để theo dõi trạng thái snooze

        if (futureSnoozeConditionOptional.isPresent()) {
            FutureSnoozeCondition condition = futureSnoozeConditionOptional.get();
            LocalDateTime now = LocalDateTime.now();

            // Kiểm tra loại snooze và đánh giá điều kiện
            switch (condition.getSnoozeType()) {
                case "ONE_TIME":
                    // Kiểm tra nếu thời gian hiện tại là trước thời gian kết thúc
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("One-time snooze for future symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                case "ONCE_IN_DURATION":
                    // Kiểm tra nếu thời gian hiện tại nằm trong khoảng start và end
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("Duration snooze for future symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                case "SPECIFIC_TIME":
                    // Kiểm tra nếu thời gian hiện tại nằm trong phạm vi 30 phút của thời gian cụ thể
                    LocalDateTime specificTime = LocalDateTime.parse(condition.getSpecificTime());
                    if (now.isBefore(specificTime.plusMinutes(30)) && now.isAfter(specificTime.minusMinutes(30))) {
                        System.out.println("Specific time snooze for future symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                default:
                    System.out.println("Unknown snooze type for future symbol: " + symbol);
                    break;
            }
        }

        return snoozeActive;
    }

    public boolean checkPriceDifferenceSnooze(String symbol, String username) {
        // Tìm PriceDifferenceSnoozeCondition theo symbol
        Optional<PriceDifferenceSnoozeCondition> priceDifferenceSnoozeConditionOptional = priceDifferenceSnoozeConditionRepository
                .findBySymbolAndUsername(symbol, username);
        boolean snoozeActive = false; // Biến cờ để theo dõi trạng thái snooze

        if (priceDifferenceSnoozeConditionOptional.isPresent()) {
            PriceDifferenceSnoozeCondition condition = priceDifferenceSnoozeConditionOptional.get();
            LocalDateTime now = LocalDateTime.now();

            // Kiểm tra loại snooze và đánh giá điều kiện
            switch (condition.getSnoozeType()) {
                case "ONE_TIME":
                    // Kiểm tra nếu thời gian hiện tại là trước thời gian kết thúc
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("One-time snooze for price difference symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                case "ONCE_IN_DURATION":
                    // Kiểm tra nếu thời gian hiện tại nằm trong khoảng start và end
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("Duration snooze for price difference symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                case "SPECIFIC_TIME":
                    // Kiểm tra nếu thời gian hiện tại nằm trong phạm vi 30 phút của thời gian cụ thể
                    LocalDateTime specificTime = LocalDateTime.parse(condition.getSpecificTime());
                    if (now.isBefore(specificTime.plusMinutes(30)) && now.isAfter(specificTime.minusMinutes(30))) {
                        System.out.println("Specific time snooze for price difference symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                default:
                    System.out.println("Unknown snooze type for price difference symbol: " + symbol);
                    break;
            }
        }

        return snoozeActive;
    }
    public boolean checkFundingRateSnooze(String symbol, String username) {
        // Tìm FundingRateSnoozeCondition theo symbol
        Optional<FundingRateSnoozeCondition> fundingRateSnoozeConditionOptional = fundingRateSnoozeConditionRepository
                .findBySymbolAndUsername(symbol, username);
        boolean snoozeActive = false; // Biến cờ để theo dõi trạng thái snooze

        if (fundingRateSnoozeConditionOptional.isPresent()) {
            FundingRateSnoozeCondition condition = fundingRateSnoozeConditionOptional.get();
            LocalDateTime now = LocalDateTime.now();

            // Kiểm tra loại snooze và đánh giá điều kiện
            switch (condition.getSnoozeType()) {
                case "ONE_TIME":
                    // Kiểm tra nếu thời gian hiện tại là trước thời gian kết thúc
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("One-time snooze for funding rate symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                case "ONCE_IN_DURATION":
                    // Kiểm tra nếu thời gian hiện tại nằm trong khoảng start và end
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("Duration snooze for funding rate symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                case "SPECIFIC_TIME":
                    // Kiểm tra nếu thời gian hiện tại nằm trong phạm vi 30 phút của thời gian cụ thể
                    LocalDateTime specificTime = LocalDateTime.parse(condition.getSpecificTime());
                    if (now.isBefore(specificTime.plusMinutes(30)) && now.isAfter(specificTime.minusMinutes(30))) {
                        System.out.println("Specific time snooze for funding rate symbol: " + symbol + " is active.");
                        snoozeActive = true; // Snooze hoạt động
                    }
                    break;

                default:
                    System.out.println("Unknown snooze type for funding rate symbol: " + symbol);
                    break;
            }
        }

        // Trả về trạng thái snooze (true nếu có snooze hoạt động, false nếu không)
        return snoozeActive;
    }

}
