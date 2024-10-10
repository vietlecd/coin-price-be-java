package com.javaweb.service.impl;

import com.javaweb.dto.TriggerConditionDTO;
import com.javaweb.helpers.trigger.*;
import com.javaweb.models.TriggerCondition;
import com.javaweb.repository.TriggerConditionRepository;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TriggerDataService {

    @Autowired
    private TriggerConditionRepository triggerConditionRepository;
    @Autowired
    private MapperHelper mapperHelper;
    @Autowired
    private ComparisonHelper comparisonHelper;
    @Autowired
    private ActionHelper actionHelper;
    @Autowired
    private UpdateHelper updateHelper;

    public void saveTriggerCondition(TriggerConditionDTO triggerConditionDTO) {
        TriggerCondition triggerCondition = mapperHelper.toEntity(triggerConditionDTO);
        triggerConditionRepository.save(triggerCondition);
    }

    public TriggerConditionDTO getTriggerConditionBySymbol(String symbol) {
        TriggerCondition triggerCondition = triggerConditionRepository.findBySymbol(symbol);
        return mapperHelper.toDTO(triggerCondition);
    }

    public void updateTriggerCondition(String symbol, TriggerConditionDTO triggerConditionDTO) {
        TriggerCondition existingTrigger = triggerConditionRepository.findBySymbol(symbol);
        updateHelper.updateDoubleFieldIfNotZero(existingTrigger::getThresholdValue, existingTrigger::setThresholdValue, triggerConditionDTO.getThresholdValue());
        updateHelper.updateFieldIfNotNull(existingTrigger::getComparisonOperator, existingTrigger::setComparisonOperator, triggerConditionDTO.getComparisonOperator());
        updateHelper.updateFieldIfNotNull(existingTrigger::getAction, existingTrigger::setAction, triggerConditionDTO.getAction());

        triggerConditionRepository.save(existingTrigger);
    }

    public void deleteTriggerCondition(String symbol) {
        triggerConditionRepository.deleteBySymbol(symbol);
    }

    public void evaluateAndTrigger(TriggerCondition triggerCondition, String currentValue) {
        if (comparisonHelper.checkCondition(triggerCondition, currentValue)) {
            actionHelper.executeAction(triggerCondition.getAction(), triggerCondition);
        }
    }
}

