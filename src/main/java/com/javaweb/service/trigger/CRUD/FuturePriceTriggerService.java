//package com.javaweb.service.trigger.CRUD;
//
////import com.javaweb.dto.trigger.FuturePriceTriggerDTO;
//import com.javaweb.helpers.trigger.TriggerMapHelper;
//import com.javaweb.model.trigger.FuturePriceTrigger;
//import com.javaweb.repository.FuturePriceTriggerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class FuturePriceTriggerService {
//    @Autowired
//    private FuturePriceTriggerRepository futurePriceTriggerRepository;
//
//    @Autowired
//    private TriggerMapHelper triggerMapHelper;
//
//    public String createTrigger(FuturePriceTriggerDTO dto, String username) {
//        FuturePriceTrigger existingTrigger = futurePriceTriggerRepository.findBySymbolAndUsername(dto.getSymbol(), username);
//
//
//        if (existingTrigger != null) {
//            futurePriceTriggerRepository.delete(existingTrigger);
//        }
//
//        FuturePriceTrigger newTrigger = triggerMapHelper.mapFuturePriceTrigger(dto);
//        newTrigger.setUsername(username);
//
//        futurePriceTriggerRepository.save(newTrigger);
//        return newTrigger.getAlert_id();
//    }
//    public void deleteTrigger(String symbol, String username) {
//        FuturePriceTrigger trigger = futurePriceTriggerRepository.findBySymbolAndUsername(symbol, username);
//        if (trigger != null) {
//            futurePriceTriggerRepository.delete(trigger);
//        } else {
//            throw new RuntimeException("Trigger not found for symbol: " + symbol);
//        }
//    }
//}
