package com.javaweb.repository;

import com.javaweb.model.trigger.UserTradingPair;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTradingPairRepository extends MongoRepository<UserTradingPair, String> {
    UserTradingPair findByUserId(String userId);
}
