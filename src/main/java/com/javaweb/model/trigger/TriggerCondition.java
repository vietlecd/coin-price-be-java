package com.javaweb.model.trigger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "trigger_type", discriminatorType = DiscriminatorType.STRING)
@Document(collection = "trigger_conditions")
public class TriggerCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "thresholdValue")
    private double thresholdValue;

    @Column(name = "comparisonOperator")
    private String comparisonOperator;

    @Column(name = "action")
    private String action;

}
