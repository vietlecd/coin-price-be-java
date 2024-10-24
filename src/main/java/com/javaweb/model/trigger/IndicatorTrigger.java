package com.javaweb.model.trigger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.scheduling.Trigger;

@Getter
@Setter
@Document(collection = "indicator_trigger")
public class IndicatorTrigger extends TriggerCondition {
    private String symbol;
    private String indicator;
    private double value;
    private String condition;
    private String notification_method;
    private int period;
}
