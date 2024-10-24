package com.javaweb.repository;

import com.javaweb.dto.snooze.IndicatorSnoozeCondition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IndicatorSnoozeConditionRepository extends MongoRepository<IndicatorSnoozeCondition, String> {

    Optional<IndicatorSnoozeCondition> findBySymbolAndUsername(String symbol, String username);
}
