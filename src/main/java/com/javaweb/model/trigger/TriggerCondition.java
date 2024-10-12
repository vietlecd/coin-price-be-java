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
public abstract class TriggerCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "comparisonOperator")
    private String comparisonOperator;

    @Column(name = "action")
    private String action;

}
