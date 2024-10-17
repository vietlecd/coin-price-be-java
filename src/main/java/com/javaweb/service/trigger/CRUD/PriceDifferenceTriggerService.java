package com.javaweb.service.trigger.CRUD;

import com.javaweb.dto.trigger.PriceDifferenceTriggerDTO;
import com.javaweb.helpers.trigger.TriggerMapHelper;
import com.javaweb.model.trigger.PriceDifferenceTrigger;
import com.javaweb.repository.PriceDifferenceTriggerRepository;
import com.javaweb.service.ITriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceDifferenceTriggerService implements ITriggerService<PriceDifferenceTriggerDTO> {

    @Autowired
    private PriceDifferenceTriggerRepository priceDifferenceTriggerRepository;

    @Autowired
    private TriggerMapHelper triggerMapHelper;

    public String createTrigger(PriceDifferenceTriggerDTO dto) {
        PriceDifferenceTrigger trigger = triggerMapHelper.mapPriceDifferenceTrigger(dto);
        PriceDifferenceTrigger savedtrigger =priceDifferenceTriggerRepository.save(trigger);
        return savedtrigger.getAlert_id();
    }
}
