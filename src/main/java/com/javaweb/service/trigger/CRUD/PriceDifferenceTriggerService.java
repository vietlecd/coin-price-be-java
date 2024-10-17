package com.javaweb.service.trigger.CRUD;

import com.javaweb.dto.trigger.PriceDifferenceTriggerDTO;
import com.javaweb.helpers.trigger.CheckSymbolExisted;
import com.javaweb.helpers.trigger.TriggerMapHelper;
import com.javaweb.model.trigger.PriceDifferenceTrigger;
import com.javaweb.repository.PriceDifferenceTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceDifferenceTriggerService {

    @Autowired
    private PriceDifferenceTriggerRepository priceDifferenceTriggerRepository;

    @Autowired
    private TriggerMapHelper triggerMapHelper;

    @Autowired
    private CheckSymbolExisted checkSymbolExisted;

    public String createTrigger(PriceDifferenceTriggerDTO dto, String username) {
        if (checkSymbolExisted.symbolExistsInPriceDifference(dto.getSymbol(), username)) {
            throw new IllegalArgumentException("Symbol already exists in database.");
        }

        PriceDifferenceTrigger trigger = triggerMapHelper.mapPriceDifferenceTrigger(dto);
        trigger.setUsername(username);

        PriceDifferenceTrigger savedtrigger = priceDifferenceTriggerRepository.save(trigger);
        return savedtrigger.getAlert_id();
    }
}
