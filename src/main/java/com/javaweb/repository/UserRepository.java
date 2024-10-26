package com.javaweb.repository;

import com.javaweb.model.mongo_entity.userData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends MongoRepository<userData, String> {
    userData findByUsername(String username);
    userData findByEmail(String email);

    void deleteByUsername(String username);
    public long count();

    //Viet them cho phan in Vip_Role của TELE.
    @Query(value = "{ 'username': ?0 }", fields = "{ 'vip_role': 1 }")
    Integer findVipRoleByUsername(String username);
}
