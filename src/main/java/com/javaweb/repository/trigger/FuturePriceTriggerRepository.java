package com.javaweb.repository.trigger;

import com.javaweb.model.trigger.FuturePriceTrigger;
import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FuturePriceTriggerRepository extends MongoRepository<FuturePriceTrigger, String> {
    FuturePriceTrigger findBySymbolAndUsername(String symbol, String username);

    List<FuturePriceTrigger> findByUsername(String username);

    @Query(value = "{}", fields = "{username: 1, symbol: 1}")
    List<FuturePriceTrigger> findAllUsernamesWithSymbols();

}
