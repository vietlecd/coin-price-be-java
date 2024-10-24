package com.javaweb.repository;

import com.javaweb.model.trigger.FundingRateInterval;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingRateIntervalRepository extends MongoRepository<FundingRateInterval, String> {
    FundingRateInterval findTopBySymbolOrderByTimestampDesc(String symbol);
    void deleteBySymbol(String symbol);
}
