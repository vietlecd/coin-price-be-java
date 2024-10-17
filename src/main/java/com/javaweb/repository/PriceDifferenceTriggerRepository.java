package com.javaweb.repository;

import com.javaweb.model.trigger.PriceDifferenceTrigger;
import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceDifferenceTriggerRepository extends MongoRepository<PriceDifferenceTrigger, String> {
    PriceDifferenceTrigger findBySymbolAndUsername(String symbol, String username);

    boolean existsBySymbolAndUsername(String symbol, String username);
}
