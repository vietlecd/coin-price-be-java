//package com.javaweb.repository;
//
//import com.javaweb.model.mongo_entity.userData;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface UserRepository  extends MongoRepository<userData, String> {
//    userData findByUsername(String username);
//    userData findByEmail(String email);
//
//    void deleteByUsername(String username);
//    public long count();
//}
