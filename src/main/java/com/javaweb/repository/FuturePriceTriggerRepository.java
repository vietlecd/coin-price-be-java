package com.javaweb.repository;

import com.javaweb.model.trigger.FuturePriceTrigger;
import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FuturePriceTriggerRepository extends MongoRepository<FuturePriceTrigger, String> {
    FuturePriceTrigger findBySymbolAndUsername(String symbol, String username);


    List<FuturePriceTrigger> findByUsername(String username);
}
