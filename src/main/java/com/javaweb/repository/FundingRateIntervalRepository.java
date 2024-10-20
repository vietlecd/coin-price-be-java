package com.javaweb.repository;

import com.javaweb.model.trigger.FundingRateInterval;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FundingRateIntervalRepository extends MongoRepository<FundingRateInterval, String> {
    FundingRateInterval findTopBySymbolOrderByTimestampDesc(String symbol);  // Lấy interval gần nhất cho 1 symbol
}
