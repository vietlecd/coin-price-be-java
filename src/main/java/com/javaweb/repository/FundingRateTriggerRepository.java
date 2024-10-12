package com.javaweb.repository;

import com.javaweb.model.trigger.FundingRateTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FundingRateTriggerRepository extends MongoRepository<FundingRateTrigger, String> {
    FundingRateTrigger findBySymbol(String symbol);
}