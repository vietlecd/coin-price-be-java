package com.javaweb.model.trigger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Document(collection = "funding_trigger")
public class FundingRateTrigger extends TriggerCondition {

    private double fundingRateThreshold;
    private double fundingRateInterval;

}
