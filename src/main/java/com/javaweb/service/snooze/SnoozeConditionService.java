package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.SnoozeCondition;
import com.javaweb.repository.SnoozeConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class SnoozeConditionService {

    @Autowired
    private SnoozeConditionRepository snoozeConditionRepository;

    // Method to check if snooze is active for a given trigger




    public boolean isSnoozeActive(String triggerId) {
        Optional<SnoozeCondition> optionalCondition = snoozeConditionRepository.findByTriggerId(triggerId);

        // Kiểm tra nếu condition tồn tại
        if (optionalCondition.isPresent()) {
            SnoozeCondition condition = optionalCondition.get();
            LocalDateTime now = LocalDateTime.now();

            switch (condition.getSnoozeType()) {
                case "ONE_TIME":
                    // Nếu là one-time và endTime vẫn chưa qua, snooze vẫn hoạt động
                    if (now.isBefore(condition.getEndTime())) {
                        return true; // Snooze đang hoạt động
                    }
                    break;

                case "ONCE_IN_DURATION":
                    // Nếu trong khoảng thời gian đã định (duration), snooze vẫn hoạt động
                    if (now.isBefore(condition.getEndTime())) {
                        return true; // Snooze đang hoạt động
                    }
                    break;



                case "SPECIFIC_TIME":
                    // Kiểm tra thời gian cụ thể (specific time)
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalDateTime specificTime = LocalDateTime.parse(condition.getSpecificTime(), formatter).withYear(now.getYear()).withMonth(now.getMonthValue()).withDayOfMonth(now.getDayOfMonth());

                    if (now.isBefore(specificTime.plusMinutes(30)) && now.isAfter(specificTime.minusMinutes(30))) {
                        // Nếu thời gian hiện tại nằm trong khoảng thời gian cho phép xung quanh thời gian cụ thể (ví dụ 30 phút trước hoặc sau)
                        return true; // Snooze đang hoạt động
                    }
                    break;

                default:
                    // Các loại khác (nếu có) có thể xử lý ở đây
                    break;
            }
        }

        return false; // Không có snooze nào đang hoạt động
    }


    // Method to create a new snooze condition
    public SnoozeCondition createSnoozeCondition(SnoozeCondition snoozeCondition) {
        return snoozeConditionRepository.save(snoozeCondition);
    }

    // Method to deactivate (delete) a snooze condition by triggerId
    public void deleteSnoozeConditionByTriggerId(String triggerId) {
        Optional<SnoozeCondition> snoozeCondition = snoozeConditionRepository.findByTriggerId(triggerId);
        snoozeCondition.ifPresent(condition -> snoozeConditionRepository.delete(condition));
    }
}
