package com.javaweb.repository;

import com.javaweb.dto.snooze.FutureSnoozeCondition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FutureSnoozeConditionRepository extends MongoRepository<FutureSnoozeCondition, String> {
    Optional<FutureSnoozeCondition> findBySymbol(String symbol);
}
