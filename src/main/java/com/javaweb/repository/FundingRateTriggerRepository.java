package com.javaweb.repository;

import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FundingRateTriggerRepository extends MongoRepository<FundingRateTrigger, String> {
    FundingRateTrigger findBySymbolAndUsername(String symbol, String username);

    List<FundingRateTrigger> findByUsername(String username);

    @Query(value = "{}", fields = "{username: 1, symbol: 1}")
    List<FundingRateTrigger> findAllUsernamesWithSymbols();
}