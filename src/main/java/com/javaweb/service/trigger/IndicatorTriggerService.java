package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.IndicatorTriggerDTO;
import com.javaweb.helpers.trigger.TriggerMapHelper;
import com.javaweb.model.trigger.IndicatorTrigger;
import com.javaweb.repository.IndicatorTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndicatorTriggerService {
    @Autowired
    private IndicatorTriggerRepository indicatorTriggerRepository;

    @Autowired
    private TriggerMapHelper triggerMapHelper;

    public String createTrigger(IndicatorTriggerDTO dto, String username) {
        IndicatorTrigger existingTrigger = indicatorTriggerRepository.findBySymbolAndUsername(dto.getSymbol(), username);

        if (existingTrigger != null) {
            indicatorTriggerRepository.delete(existingTrigger);
        }

        IndicatorTrigger newTrigger = triggerMapHelper.mapIndicatorTrigger(dto);
        newTrigger.setUsername(username);

        indicatorTriggerRepository.save(newTrigger);
        return newTrigger.getAlert_id();
    }
}
