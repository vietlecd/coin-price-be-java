package com.javaweb.service.snooze;

import com.javaweb.dto.snooze.SnoozeCondition;
import com.javaweb.repository.SnoozeConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;  // Sử dụng java.util.Optional

@Service
public class SnoozeConditionService {

    @Autowired
    private SnoozeConditionRepository snoozeConditionRepository;

    // Method to check if snooze is active for a given trigger
    public boolean isSnoozeActive(String triggerId) {
        List<SnoozeCondition> conditions = snoozeConditionRepository.findByTriggerIdAndActive(triggerId, true);

        for (SnoozeCondition condition : conditions) {
            if (LocalDateTime.now().isBefore(condition.getEndTime())) {
                // Snooze đang hoạt động
                return true; // Snooze is active
            }
        }
        return false; // No active snooze
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
