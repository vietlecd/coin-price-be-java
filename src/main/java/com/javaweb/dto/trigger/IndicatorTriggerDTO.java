package com.javaweb.dto.trigger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = IndicatorTriggerDTO.Builder.class)
public class IndicatorTriggerDTO {
    private final String symbol;
    private final String indicator;
    private final double value;
    private final String condition;
    private final int period;
    private final String notification_method;

    private IndicatorTriggerDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.indicator = builder.indicator;
        this.value = builder.value;
        this.condition = builder.condition;
        this.period = builder.period;
        this.notification_method = builder.notification_method;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String symbol;
        private String indicator;
        private double value;
        private String condition;
        private int period;
        private String notification_method;

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }
        public Builder setIndicator(String indicator) {
            this.indicator = indicator;
            return this;
        }
        public Builder setValue(double value) {
            this.value = value;
            return this;
        }
        public Builder setCondition(String condition) {
            this.condition = condition;
            return this;
        }
        public Builder setPeriod(int period) {
            this.period = period;
            return this;
        }
        public Builder setNotification_method(String notification_method) {
            this.notification_method = notification_method;
            return this;
        }

        public IndicatorTriggerDTO build() {
            return new IndicatorTriggerDTO(this);
        }
    }
}
