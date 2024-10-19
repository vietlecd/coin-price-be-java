package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.IndicatorTriggerDTO;
import com.javaweb.helpers.trigger.CheckSymbolExisted;
import com.javaweb.helpers.trigger.TriggerMapHelper;
import com.javaweb.model.trigger.FundingRateTrigger;
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

    @Autowired
    private CheckSymbolExisted checkSymbolExisted;

    public String createTrigger(IndicatorTriggerDTO dto) {
        if (checkSymbolExisted.symbolExistsInFundingRate(dto.getSymbol())) {
            throw new IllegalArgumentException("Symbol already exists in database.");
        }
        IndicatorTrigger trigger= triggerMapHelper.mapIndicatorTrigger(dto);
        IndicatorTrigger savedTrigger = indicatorTriggerRepository.save(trigger);
        return savedTrigger.getAlert_id();
    }
}
