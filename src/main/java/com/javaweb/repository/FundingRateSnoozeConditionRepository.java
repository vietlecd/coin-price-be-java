package com.javaweb.repository;

import com.javaweb.dto.snooze.FundingRateSnoozeCondition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FundingRateSnoozeConditionRepository extends MongoRepository<FundingRateSnoozeCondition, String> {
    Optional<FundingRateSnoozeCondition> findBySymbol(String symbol);
}
