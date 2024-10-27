//package com.javaweb.service.trigger.CRUD;
//
//import com.javaweb.dto.trigger.SpotPriceTriggerDTO;
//import com.javaweb.helpers.trigger.TriggerMapHelper;
//import com.javaweb.model.trigger.SpotPriceTrigger;
//import com.javaweb.repository.SpotPriceTriggerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SpotPriceTriggerService {
//
//    @Autowired
//    private SpotPriceTriggerRepository spotPriceTriggerRepository;
//
//    @Autowired
//    private TriggerMapHelper triggerMapHelper;
//
//
//    public String createTrigger(SpotPriceTriggerDTO dto, String username) {
//        SpotPriceTrigger existingTrigger = spotPriceTriggerRepository.findBySymbolAndUsername(dto.getSymbol(), username);
//
//        if (existingTrigger != null) {
//            spotPriceTriggerRepository.delete(existingTrigger);
//        }
//
//        SpotPriceTrigger newTrigger = triggerMapHelper.mapSpotPriceTrigger(dto);
//        newTrigger.setUsername(username);
//
//        spotPriceTriggerRepository.save(newTrigger);
//        return newTrigger.getAlert_id();
//    }
//
//    public void deleteTrigger(String symbol, String username) {
//        SpotPriceTrigger trigger = spotPriceTriggerRepository.findBySymbolAndUsername(symbol, username);
//        if (trigger != null) {
//            spotPriceTriggerRepository.delete(trigger);
//        } else {
//            throw new RuntimeException("Trigger not found for symbol: " + symbol);
//        }
//    }
//}
