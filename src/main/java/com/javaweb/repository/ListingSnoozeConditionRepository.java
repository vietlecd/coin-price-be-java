package com.javaweb.repository;

import com.javaweb.dto.snooze.ListingSnoozeCondition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ListingSnoozeConditionRepository extends MongoRepository<ListingSnoozeCondition, String> {

    Optional<ListingSnoozeCondition> findBySymbolAndUsername(String symbol, String username);
}
