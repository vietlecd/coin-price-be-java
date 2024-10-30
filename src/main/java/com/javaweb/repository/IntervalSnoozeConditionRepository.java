package com.javaweb.repository;

import com.javaweb.dto.snooze.IntervalSnoozeCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IntervalSnoozeConditionRepository extends MongoRepository<IntervalSnoozeCondition, String> {

    Optional<IntervalSnoozeCondition> findBySymbolAndUsername(String symbol, String username);
}
