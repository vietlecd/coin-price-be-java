package com.javaweb.repository;

import com.javaweb.model.trigger.PriceDifferenceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceDifferenceTriggerRepository extends MongoRepository<PriceDifferenceTrigger, String> {
}
