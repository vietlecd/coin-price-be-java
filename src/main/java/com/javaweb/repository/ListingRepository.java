package com.javaweb.repository;

import com.javaweb.model.mongo_entity.ListingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ListingRepository extends MongoRepository<ListingEntity, String> {


    boolean existsBySymbol(String symbol);


    void deleteBySymbol(String symbol);


    boolean existsByNotificationMethod(String notificationMethod);


    void deleteByNotificationMethod(String notificationMethod);

    ListingEntity findBySymbol(String symbol);
}
