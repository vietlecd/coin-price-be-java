package com.javaweb.model.trigger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "funding_rate_trigger")
public class FundingRateTrigger extends TriggerCondition {
    private double fundingRateThreshold;
    private boolean fundingRateInterval;
    private String triggerType = "funding-rate";
}
