package com.javaweb.repository.trigger;

import com.javaweb.model.trigger.FundingInterval;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingIntervalRepository extends MongoRepository<FundingInterval, String> {

    // Phương thức để tìm FundingInterval theo symbol
    FundingInterval findBySymbol(String symbol);

    // Phương thức để xóa FundingInterval theo symbol
    void deleteBySymbol(String symbol);
}
