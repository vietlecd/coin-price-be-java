package com.javaweb.model.trigger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.scheduling.Trigger;

@Getter
@Setter
@Document(collection = "indicator_trigger")
public class IndicatorTrigger extends TriggerCondition {
    private String indicator;
    private int period;
}
