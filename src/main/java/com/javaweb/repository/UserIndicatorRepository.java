package com.javaweb.repository;

import com.javaweb.model.mongo_entity.userIndicator;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserIndicatorRepository extends MongoRepository<userIndicator, String> {
    Optional<userIndicator> findByUsernameAndName(String username, String name);
}
