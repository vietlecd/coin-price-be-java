package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.PriceDifferenceTriggerDTO;
import com.javaweb.helpers.trigger.TriggerHelper;
import com.javaweb.model.trigger.PriceDifferenceTrigger;
import com.javaweb.repository.TriggerConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceDifferenceTriggerService {

    @Autowired
    private TriggerConditionRepository triggerConditionRepository;

    @Autowired
    private TriggerHelper triggerHelper;

    public PriceDifferenceTrigger createTrigger(PriceDifferenceTriggerDTO dto) {
        PriceDifferenceTrigger trigger = triggerHelper.mapPriceDifferenceTrigger(dto);
        return triggerConditionRepository.save(trigger);
    }
}
