package com.javaweb.model.trigger;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("SPOT_FUTURE_PRICE")
public class SpotFuturePriceTrigger extends TriggerCondition {
    private double spotPriceThreshold;
    private double futurePriceThreshold;
}
