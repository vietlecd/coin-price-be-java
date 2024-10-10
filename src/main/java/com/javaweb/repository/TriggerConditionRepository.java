package com.javaweb.repository;

import com.javaweb.model.mongo_entity.userData;
import com.javaweb.models.TriggerCondition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TriggerConditionRepository extends MongoRepository<TriggerCondition, String> {
    TriggerCondition findBySymbol(String symbol);

    void deleteBySymbol(String symbol);
}
