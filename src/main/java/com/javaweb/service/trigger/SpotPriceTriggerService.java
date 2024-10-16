package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.SpotPriceTriggerDTO;
import com.javaweb.helpers.trigger.CheckSymbolExisted;
import com.javaweb.helpers.trigger.TriggerMapHelper;
import com.javaweb.model.trigger.SpotPriceTrigger;
import com.javaweb.repository.SpotPriceTriggerRepository;
import com.javaweb.service.ITriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpotPriceTriggerService {

    @Autowired
    private SpotPriceTriggerRepository spotPriceTriggerRepository;

    @Autowired
    private TriggerMapHelper triggerMapHelper;

    @Autowired
    private CheckSymbolExisted checkSymbolExisted;

    public String createTrigger(SpotPriceTriggerDTO dto, String username) {
        if (checkSymbolExisted.symbolExistsInSpot(dto.getSymbol(), username)) {
            throw new IllegalArgumentException("Symbol already exists in database.");
        }
        SpotPriceTrigger trigger = triggerMapHelper.mapSpotPriceTrigger(dto);
        trigger.setUsername(username);

        SpotPriceTrigger savedTrigger = spotPriceTriggerRepository.save(trigger);
        return savedTrigger.getAlert_id();
    }
}
