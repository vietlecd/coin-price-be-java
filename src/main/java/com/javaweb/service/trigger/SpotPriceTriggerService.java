package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.SpotPriceTriggerDTO;
import com.javaweb.dto.trigger.SpotPriceTriggerDTO;
import com.javaweb.helpers.trigger.TriggerHelper;
import com.javaweb.model.trigger.SpotPriceTrigger;
import com.javaweb.repository.SpotPriceTriggerRepository;
import com.javaweb.service.ITriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpotPriceTriggerService implements ITriggerService<SpotPriceTriggerDTO> {

    @Autowired
    private SpotPriceTriggerRepository spotPriceTriggerRepository;

    @Autowired
    private TriggerHelper triggerHelper;

    public void createTrigger(SpotPriceTriggerDTO dto) {
        SpotPriceTrigger trigger = triggerHelper.mapSpotPriceTrigger(dto);
        spotPriceTriggerRepository.save(trigger);
    }
}
