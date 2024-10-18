package com.javaweb.dto.trigger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = IndicatorTriggerDTO.Builder.class)
public class IndicatorTriggerDTO {
    private final String symbol;
    private final String indicatorType;     // MA, EMA, BOLL
    private final String triggerType;       // "cross-above", "cross-below", "overbought", "oversold"
    private final int shortTermPeriod;
    private final int longTermPeriod;

    private IndicatorTriggerDTO(Builder builder) {
        this.symbol = builder.symbol;
        this.indicatorType = builder.triggerType;
        this.triggerType = builder.condition;
        this.shortTermPeriod = builder.shortTermPeriod;
        this.longTermPeriod = builder.longTermPeriod;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String symbol;
        private String triggerType;
        private String condition;
        private int shortTermPeriod;
        private int longTermPeriod;

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }
        public Builder setTriggerType(String triggerType) {
            this.triggerType = triggerType;
            return this;
        }
        public Builder setCondition(String condition) {
            this.condition = condition;
            return this;
        }
        public Builder setShortTermPeriod(int shortTermPeriod) {
            this.shortTermPeriod = shortTermPeriod;
            return this;
        }
        public Builder setLongTermPeriod(int longTermPeriod) {
            this.longTermPeriod = longTermPeriod;
            return this;
        }

        public IndicatorTriggerDTO build() {
            return new IndicatorTriggerDTO(this);
        }
    }
}
