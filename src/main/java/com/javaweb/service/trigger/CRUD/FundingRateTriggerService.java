package com.javaweb.service.trigger.CRUD;

import com.javaweb.dto.trigger.FundingRateTriggerDTO;
import com.javaweb.helpers.trigger.TriggerMapHelper;
import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.repository.trigger.FundingRateTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundingRateTriggerService {
    @Autowired
    private FundingRateTriggerRepository fundingRateTriggerRepository;

    @Autowired
    private TriggerMapHelper triggerMapHelper;

    public String createTrigger(FundingRateTriggerDTO dto, String username) {
        FundingRateTrigger existingTrigger = fundingRateTriggerRepository.findBySymbolAndUsername(dto.getSymbol(), username);

        if (existingTrigger != null) {
            fundingRateTriggerRepository.delete(existingTrigger);
        }

        FundingRateTrigger newTrigger = triggerMapHelper.mapFundingRateTrigger(dto);
        newTrigger.setUsername(username);

        fundingRateTriggerRepository.save(newTrigger);
        return newTrigger.getAlert_id();
    }

    public void deleteTrigger(String symbol, String username) {
        FundingRateTrigger trigger = fundingRateTriggerRepository.findBySymbolAndUsername(symbol, username);
        if (trigger != null) {
            fundingRateTriggerRepository.delete(trigger);
        } else {
            throw new RuntimeException("Trigger not found for symbol: " + symbol);
        }
    }
}
