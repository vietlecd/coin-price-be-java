package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.FundingRateTriggerDTO;
import com.javaweb.helpers.trigger.TriggerHelper;
import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.repository.FundingRateTriggerRepository;
import com.javaweb.service.ITriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundingRateTriggerService implements ITriggerService<FundingRateTriggerDTO> {
    @Autowired
    private FundingRateTriggerRepository fundingRateTriggerRepository;

    @Autowired
    private TriggerHelper triggerHelper;

    public void createTrigger(FundingRateTriggerDTO dto) {
        FundingRateTrigger trigger = triggerHelper.mapFundingRateTrigger(dto);
        fundingRateTriggerRepository.save(trigger);
    }
}
