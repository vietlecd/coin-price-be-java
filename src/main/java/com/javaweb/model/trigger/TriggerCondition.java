package com.javaweb.model.trigger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@Getter
@Setter
public abstract class TriggerCondition {
    @Id
    private String id;  // Let MongoDB manage this field

    private String alert_id;

    private String username;

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
