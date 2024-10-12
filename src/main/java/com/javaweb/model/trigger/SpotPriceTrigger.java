package com.javaweb.model.trigger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Document(collection = "spot_price_trigger")
public class SpotPriceTrigger extends TriggerCondition {
    private double spotPriceThreshold;
}
