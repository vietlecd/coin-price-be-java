package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.FutureSnoozeCondition;
import com.javaweb.repository.FutureSnoozeConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class FutureSnoozeConditionService {

    @Autowired
    private FutureSnoozeConditionRepository futureSnoozeConditionRepository;

    // Phương thức để kiểm tra nếu điều kiện snooze đang hoạt động cho một trigger
    public boolean isSnoozeActive(String symbol) {
        Optional<FutureSnoozeCondition> optionalCondition = futureSnoozeConditionRepository.findBySymbol(symbol);

        // Kiểm tra nếu điều kiện snooze tồn tại
        if (optionalCondition.isPresent()) {
            FutureSnoozeCondition condition = optionalCondition.get();
            LocalDateTime now = LocalDateTime.now();

            switch (condition.getSnoozeType()) {
                case "ONE_TIME":
                    // Nếu là one-time và thời gian snooze vẫn còn hoạt động
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("Snooze active for symbol: " + symbol + " type: ");
                        return true;
                    }
                    break;

                case "ONCE_IN_DURATION":
                    // Nếu điều kiện snooze đang hoạt động theo khoảng thời gian
                    if (now.isBefore(condition.getEndTime())) {
                        System.out.println("Snooze active for symbol: " + symbol + " type: ");
                        return true;
                    }
                    break;

                case "SPECIFIC_TIME":
                    // Kiểm tra thời gian cụ thể (specific time) theo giờ định trước
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalDateTime specificTime = LocalDateTime.parse(condition.getSpecificTime(), formatter)
                            .withYear(now.getYear()).withMonth(now.getMonthValue()).withDayOfMonth(now.getDayOfMonth());

                    // Nếu thời gian hiện tại nằm trong khoảng 30 phút trước và sau thời gian cụ thể
                    if (now.isBefore(specificTime.plusMinutes(30)) && now.isAfter(specificTime.minusMinutes(30))) {
                        System.out.println("Snooze active for symbol: " + symbol + " type: ");
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

    // Phương thức để tạo điều kiện snooze mới
    public FutureSnoozeCondition createSnoozeCondition(FutureSnoozeCondition futureSnoozeCondition) {
        Optional<FutureSnoozeCondition> existingSnoozeCondition = futureSnoozeConditionRepository.findBySymbol(futureSnoozeCondition.getSymbol());

        if (existingSnoozeCondition.isPresent()) {
            // Nếu symbol đã tồn tại, ném ngoại lệ hoặc trả về một phản hồi tuỳ chỉnh
            throw new IllegalArgumentException("Symbol '" + futureSnoozeCondition.getSymbol() + "' already exists in the database");
        }

        // Nếu symbol chưa tồn tại, lưu điều kiện snooze mới
        return futureSnoozeConditionRepository.save(futureSnoozeCondition);
    }

    // Phương thức để hủy kích hoạt (xóa) điều kiện snooze theo triggerId
    public void deleteSnoozeConditionByTriggerId(String triggerId) {
        Optional<FutureSnoozeCondition> snoozeCondition = futureSnoozeConditionRepository.findBySymbol(triggerId);
        snoozeCondition.ifPresent(condition -> futureSnoozeConditionRepository.delete(condition));
    }
}
