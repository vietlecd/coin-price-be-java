package com.javaweb.model.trigger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Document(collection = "future_price_trigger")
public class FuturePriceTrigger extends TriggerCondition {
    private double futurePriceThreshold;


}
