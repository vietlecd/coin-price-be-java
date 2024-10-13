package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.SpotPriceTriggerDTO;
import com.javaweb.helpers.trigger.TriggerMapHelper;
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
    private TriggerMapHelper triggerMapHelper;

    public void createTrigger(SpotPriceTriggerDTO dto) {
        SpotPriceTrigger trigger = triggerMapHelper.mapSpotPriceTrigger(dto);
        spotPriceTriggerRepository.save(trigger);
    }
}
