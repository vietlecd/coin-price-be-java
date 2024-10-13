package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.FundingRateTriggerDTO;
import com.javaweb.helpers.trigger.TriggerMapHelper;
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
    private TriggerMapHelper triggerMapHelper;

    public void createTrigger(FundingRateTriggerDTO dto) {
        FundingRateTrigger trigger = triggerMapHelper.mapFundingRateTrigger(dto);
        fundingRateTriggerRepository.save(trigger);
    }
}
