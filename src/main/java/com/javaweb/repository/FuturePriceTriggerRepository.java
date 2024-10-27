//package com.javaweb.repository;
//
//import com.javaweb.model.trigger.FuturePriceTrigger;
//import com.javaweb.model.trigger.SpotPriceTrigger;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//
//import java.util.List;
//
//public interface FuturePriceTriggerRepository extends MongoRepository<FuturePriceTrigger, String> {
//    FuturePriceTrigger findBySymbolAndUsername(String symbol, String username);
//
////<<<<<<< HEAD
//
//    List<FuturePriceTrigger> findByUsername(String username);
//
//    @Query(value = "{}", fields = "{username: 1, symbol: 1}")
//    List<FuturePriceTrigger> findAllUsernamesWithSymbols();
////=======
////>>>>>>> 7a466b8b04f4fd9b915d5b86c7a4a2cc1f94642c
//}
