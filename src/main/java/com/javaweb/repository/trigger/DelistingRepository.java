package com.javaweb.repository.trigger;

import com.javaweb.model.trigger.DelistingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DelistingRepository extends MongoRepository<DelistingEntity, String> {

    boolean existsByTitle(String title);

    void deleteByTitle(String title);

    boolean existsByNotificationMethod(String notificationMethod);

    void deleteByNotificationMethod(String notificationMethod);

    DelistingEntity findByTitle(String title);
}
