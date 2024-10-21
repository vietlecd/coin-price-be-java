package com.javaweb.repository;

import com.javaweb.model.trigger.SpotPriceTrigger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
<<<<<<< HEAD
import java.util.Map;
import java.util.Optional;
=======
>>>>>>> 7a466b8b04f4fd9b915d5b86c7a4a2cc1f94642c

public interface SpotPriceTriggerRepository extends MongoRepository<SpotPriceTrigger, String> {
    SpotPriceTrigger findBySymbolAndUsername(String symbol, String username);

    List<SpotPriceTrigger> findByUsername(String username);


    @Query(value = "{}", fields = "{username: 1, symbol: 1}")
    List<SpotPriceTrigger> findAllUsernamesWithSymbols();
}
