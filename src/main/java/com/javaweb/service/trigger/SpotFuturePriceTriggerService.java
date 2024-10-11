package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.SpotFuturePriceTriggerDTO;
import com.javaweb.helpers.trigger.TriggerHelper;
import com.javaweb.model.trigger.SpotFuturePriceTrigger;
import com.javaweb.repository.TriggerConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpotFuturePriceTriggerService {

    @Autowired
    private TriggerConditionRepository triggerConditionRepository;

    @Autowired
    private TriggerHelper triggerHelper;

    public SpotFuturePriceTrigger createTrigger(SpotFuturePriceTriggerDTO dto) {
        SpotFuturePriceTrigger trigger = triggerHelper.mapSpotFuturePriceTrigger(dto);
        return triggerConditionRepository.save(trigger);
    }
}
