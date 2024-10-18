package com.javaweb.repository;

import com.javaweb.dto.snooze.SpotSnoozeCondition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpotSnoozeConditionRepository extends MongoRepository<SpotSnoozeCondition, String> {
    Optional<SpotSnoozeCondition> findBySymbolAndUsernameId(String symbol,String Type);
}
