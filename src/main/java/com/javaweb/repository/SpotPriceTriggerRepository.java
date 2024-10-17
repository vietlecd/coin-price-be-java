package com.javaweb.repository;

import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpotPriceTriggerRepository extends MongoRepository<SpotPriceTrigger, String> {
    SpotPriceTrigger findBySymbolAndUsername(String symbol, String username);

    boolean existsBySymbolAndUsername(String symbol, String username);
}
