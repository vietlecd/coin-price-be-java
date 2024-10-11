package com.javaweb.repository;

import com.javaweb.model.trigger.TriggerCondition;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TriggerConditionRepository extends MongoRepository<TriggerCondition, String> {
    TriggerCondition findBySymbol(String symbol);

    void deleteBySymbol(String symbol);
}
