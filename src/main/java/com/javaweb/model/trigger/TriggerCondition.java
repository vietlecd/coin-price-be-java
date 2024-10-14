package com.javaweb.model.trigger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
public abstract class TriggerCondition {
    @Id
    private String alert_id;

    private String symbol;

    private String condition;

    private String notification_method;

    public TriggerCondition() {
        this.alert_id = generateAlertId();
    }

    private String generateAlertId() {
        return UUID.randomUUID().toString();
    }

}
