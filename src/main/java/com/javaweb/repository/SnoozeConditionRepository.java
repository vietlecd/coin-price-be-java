package com.javaweb.repository;


import com.javaweb.dto.snooze.SnoozeCondition;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface SnoozeConditionRepository extends MongoRepository<SnoozeCondition, String> {
    List<SnoozeCondition> findByTriggerIdAndActive(String triggerId, boolean active);
    Optional<SnoozeCondition> findByTriggerId(String triggerId);
}
