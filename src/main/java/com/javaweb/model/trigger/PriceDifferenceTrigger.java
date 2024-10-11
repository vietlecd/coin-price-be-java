package com.javaweb.model.trigger;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("PRICE_DIFFERENCE")
public class PriceDifferenceTrigger extends TriggerCondition {
    private double priceDifferenceThreshold;
}
