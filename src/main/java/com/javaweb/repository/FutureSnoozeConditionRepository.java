//package com.javaweb.repository;
//
//import com.javaweb.dto.snooze.FutureSnoozeCondition;
//import com.javaweb.dto.snooze.SpotSnoozeCondition;
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//import java.util.Optional;
//
//public interface FutureSnoozeConditionRepository extends MongoRepository<FutureSnoozeCondition, String> {
//    Optional<FutureSnoozeCondition> findBySymbolAndUsername(String symbol, String username);
//}
