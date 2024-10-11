package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.FundingRateTriggerDTO;
import com.javaweb.helpers.trigger.TriggerHelper;
import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.repository.TriggerConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundingRateTriggerService {
    @Autowired
    private TriggerConditionRepository triggerConditionRepository;

    @Autowired
    private TriggerHelper triggerHelper;

    public FundingRateTrigger createTrigger(FundingRateTriggerDTO dto) {
        FundingRateTrigger trigger = triggerHelper.mapFundingRateTrigger(dto);
        return triggerConditionRepository.save(trigger);
    }
}
