package com.javaweb.repository;

import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SpotPriceTriggerRepository extends MongoRepository<SpotPriceTrigger, String> {
    SpotPriceTrigger findBySymbolAndUsername(String symbol, String username);

    List<SpotPriceTrigger> findByUsername(String username);


    @Query(value = "{}", fields = "{username: 1, symbol: 1}")
    List<SpotPriceTrigger> findAllUsernamesWithSymbols();
}
