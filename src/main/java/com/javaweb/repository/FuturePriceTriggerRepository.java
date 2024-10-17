package com.javaweb.repository;

import com.javaweb.model.trigger.FuturePriceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FuturePriceTriggerRepository extends MongoRepository<FuturePriceTrigger, String> {
    FuturePriceTrigger findBySymbolAndUsername(String symbol, String username);

    boolean existsBySymbolAndUsername(String symbol, String username);
}
