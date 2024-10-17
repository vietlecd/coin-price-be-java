package com.javaweb.repository;

import com.javaweb.model.trigger.FundingRateTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FundingRateTriggerRepository extends MongoRepository<FundingRateTrigger, String> {
    FundingRateTrigger findBySymbolAndUsername(String symbol, String username);

    boolean existsBySymbolAndUsername(String symbol, String username);
}