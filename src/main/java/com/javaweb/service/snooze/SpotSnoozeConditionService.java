package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.SpotSnoozeCondition;
import com.javaweb.repository.SpotSnoozeConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class SpotSnoozeConditionService {

    @Autowired
    private SpotSnoozeConditionRepository spotSnoozeConditionRepository;

    // Method to check if snooze is active for a given trigger




    public boolean isSnoozeActive(String symbol) {
        Optional<SpotSnoozeCondition> optionalCondition = spotSnoozeConditionRepository.findBySymbol(symbol);

        // Kiểm tra nếu điều kiện snooze tồn tại
        if (optionalCondition.isPresent()) {
            SpotSnoozeCondition condition = optionalCondition.get();
            LocalDateTime now = LocalDateTime.now();

            switch (condition.getSnoozeType()) {
                case "ONE_TIME":
                    // Nếu là one-time và thời gian snooze vẫn còn hoạt động
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("Snooze active for symbol: " + symbol + " type: " );
                        return true;
                    }
                    break;

                case "ONCE_IN_DURATION":
                    // Nếu điều kiện snooze đang hoạt động theo khoảng thời gian
                    if (now.isBefore(condition.getEndTime()) ) {
                        System.out.println("Snooze active for symbol: " + symbol + " type: " );
                        return true;
                    }
                    break;



                case "SPECIFIC_TIME":
                    // Kiểm tra thời gian cụ thể (specific time) theo giờ định trước
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalDateTime specificTime = LocalDateTime.parse(condition.getSpecificTime(), formatter)
                            .withYear(now.getYear()).withMonth(now.getMonthValue()).withDayOfMonth(now.getDayOfMonth());

                    // Nếu thời gian hiện tại nằm trong khoảng 30 phút trước và sau thời gian cụ thể
                    if (now.isBefore(specificTime.plusMinutes(30)) && now.isAfter(specificTime.minusMinutes(30)) ) {
                        System.out.println("Snooze active for symbol: " + symbol + " type: " );
                        return true;
                    }
                    break;

                default:
                    System.out.println("Unknown snooze type for symbol: " + symbol);
                    break;
            }
        }

        // Không có snooze nào hoạt động cho symbol và type này
        return false;
    }

    // Method to create a new snooze condition
    public SpotSnoozeCondition createSnoozeCondition(SpotSnoozeCondition spotSnoozeCondition) {
        Optional<SpotSnoozeCondition> existingSnoozeCondition = spotSnoozeConditionRepository.findBySymbol(spotSnoozeCondition.getSymbol());

        if (existingSnoozeCondition.isPresent()) {
            // If symbol exists, throw an exception or return a custom response
            throw new IllegalArgumentException("Symbol '" + spotSnoozeCondition.getSymbol() + "' already exists in the database");
        }

        // If the symbol doesn't exist, save the new snooze condition
        return spotSnoozeConditionRepository.save(spotSnoozeCondition);
    }

    // Method to deactivate (delete) a snooze condition by triggerId
    public void deleteSnoozeConditionByTriggerId(String triggerId) {
        Optional<SpotSnoozeCondition> snoozeCondition = spotSnoozeConditionRepository.findBySymbol(triggerId);
        snoozeCondition.ifPresent(condition -> spotSnoozeConditionRepository.delete(condition));
    }
}
