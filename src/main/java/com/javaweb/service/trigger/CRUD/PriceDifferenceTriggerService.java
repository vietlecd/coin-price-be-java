package com.javaweb.service.trigger.CRUD;

import com.javaweb.dto.trigger.PriceDifferenceTriggerDTO;
import com.javaweb.helpers.trigger.TriggerMapHelper;
import com.javaweb.model.trigger.PriceDifferenceTrigger;
import com.javaweb.repository.trigger.PriceDifferenceTriggerRepository;
import com.javaweb.utils.TriggerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceDifferenceTriggerService {

    @Autowired
    private PriceDifferenceTriggerRepository priceDifferenceTriggerRepository;

    @Autowired
    private TriggerMapHelper triggerMapHelper;

    public String createTrigger(PriceDifferenceTriggerDTO dto, String username) {
        PriceDifferenceTrigger existingTrigger = priceDifferenceTriggerRepository.findBySymbolAndUsername(dto.getSymbol(), username);

        if (existingTrigger != null) {
            priceDifferenceTriggerRepository.delete(existingTrigger);
        }

        PriceDifferenceTrigger newTrigger = triggerMapHelper.mapPriceDifferenceTrigger(dto);
        newTrigger.setUsername(username);

        priceDifferenceTriggerRepository.save(newTrigger);
        return newTrigger.getAlert_id();
    }


    public void deleteTrigger(String symbol, String username) {
        PriceDifferenceTrigger trigger = priceDifferenceTriggerRepository.findBySymbolAndUsername(symbol, username);
        if (trigger != null) {
            priceDifferenceTriggerRepository.delete(trigger);
        } else {
            throw new TriggerNotFoundException("Trigger not found for symbol: " + symbol);
        }
    }
}
