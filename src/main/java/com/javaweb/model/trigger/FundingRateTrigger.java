package com.javaweb.model.trigger;

import com.javaweb.model.trigger.TriggerCondition;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

@Entity
@Getter
@Setter
@DiscriminatorValue("FUNDING_RATE")
public class FundingRateTrigger extends TriggerCondition {

    private double fundingRateThreshold;
    private double fundingRateInterval;

}
