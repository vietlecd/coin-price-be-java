package com.javaweb.model.trigger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;



@Getter
@Setter
@Document(collection = "price_difference_trigger")
public class PriceDifferenceTrigger extends TriggerCondition {
    private double priceDifferenceThreshold;
    private String triggerType = "price-difference";
}
