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
public abstract class TriggerCondition {
    @Id
    private String id;

    private String symbol;

    private String comparisonOperator;

    private String action;

}
