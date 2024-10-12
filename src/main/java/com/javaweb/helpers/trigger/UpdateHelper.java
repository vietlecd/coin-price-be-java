package com.javaweb.helpers.trigger;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;


@Component
public class UpdateHelper {

    public <T> void updateFieldIfNotNull(Supplier<T> getter, Consumer<T> setter, T newValue) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }

    public void updateDoubleFieldIfNotZero(Supplier<Double> getter, Consumer<Double> setter, double newValue) {
        if (newValue != 0.0) {
            setter.accept(newValue);
        }
    }
}
