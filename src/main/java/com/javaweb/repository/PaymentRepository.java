package com.javaweb.repository;

import com.javaweb.model.mongo_entity.paymentHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PaymentRepository extends MongoRepository<paymentHistory, String> {
    paymentHistory findByDate(Date date);
    paymentHistory findByUsername(String username);
    paymentHistory findByEmail(String email);
}
