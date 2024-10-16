package com.javaweb.repository;

import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpotPriceTriggerRepository extends MongoRepository<SpotPriceTrigger, String> {
//    SpotPriceTrigger findFirstBySymbol(String symbol);

    Optional<SpotPriceTrigger> findFirstBySymbol(String symbol);


    boolean existsBySymbolAndUsername(String symbol, String username);
}
