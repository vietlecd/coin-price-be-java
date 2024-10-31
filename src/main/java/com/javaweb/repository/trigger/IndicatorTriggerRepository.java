package com.javaweb.repository.trigger;

import com.javaweb.model.trigger.IndicatorTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IndicatorTriggerRepository extends MongoRepository<IndicatorTrigger, String> {
    IndicatorTrigger findBySymbolAndUsername(String symbol, String username);

    List<IndicatorTrigger> findByUsername(String username);


    @Query(value = "{}", fields = "{username: 1, symbol: 1}")
    List<IndicatorTrigger> findAllUsernamesWithSymbols();
}
