package com.javaweb.repository;

import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpotPriceTriggerRepository extends MongoRepository<SpotPriceTrigger, String> {
    SpotPriceTrigger findBySymbol(String symbol);
}
