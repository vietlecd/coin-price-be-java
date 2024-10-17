package com.javaweb.service.trigger.CRUD;

import com.javaweb.dto.trigger.FundingRateTriggerDTO;
import com.javaweb.helpers.trigger.CheckSymbolExisted;
import com.javaweb.helpers.trigger.TriggerMapHelper;
import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.repository.FundingRateTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundingRateTriggerService {
    @Autowired
    private FundingRateTriggerRepository fundingRateTriggerRepository;

    @Autowired
    private TriggerMapHelper triggerMapHelper;

    @Autowired
    private CheckSymbolExisted checkSymbolExisted;

    public String createTrigger(FundingRateTriggerDTO dto, String username) {
        if (checkSymbolExisted.symbolExistsInFundingRate(dto.getSymbol(), username)) {
            throw new IllegalArgumentException("Symbol already exists in database.");
        }
        FundingRateTrigger trigger= triggerMapHelper.mapFundingRateTrigger(dto);
        trigger.setUsername(username);

        FundingRateTrigger savedTrigger = fundingRateTriggerRepository.save(trigger);
        return savedTrigger.getAlert_id();
    }
}
