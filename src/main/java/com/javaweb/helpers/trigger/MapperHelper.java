package com.javaweb.helpers.trigger;


import com.javaweb.dto.TriggerConditionDTO;
import com.javaweb.models.TriggerCondition;
import org.springframework.stereotype.Component;

@Component
public class MapperHelper {

    public TriggerConditionDTO toDTO(TriggerCondition entity) {
        return new TriggerConditionDTO.Builder()
                .setSymbol(entity.getSymbol())
                .setThresholdValue(entity.getThresholdValue())
                .setComparisonOperator(entity.getComparisonOperator())
                .setAction(entity.getAction())
                .build();
    }

    public TriggerCondition toEntity(TriggerConditionDTO dto) {
        TriggerCondition entity = new TriggerCondition();
        entity.setSymbol(dto.getSymbol());
        entity.setThresholdValue(dto.getThresholdValue());
        entity.setComparisonOperator(dto.getComparisonOperator());
        entity.setAction(dto.getAction());
        return entity;
    }
}
