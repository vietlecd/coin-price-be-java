package com.javaweb.service.impl;
import com.javaweb.service.IUserIndicatorService;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilationFailedException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserIndicatorService implements IUserIndicatorService {
    private final Map<String, String> indicatorMap = new HashMap<>();

    @Override
    public void addIndicator(String name, String expression) {
        validateGroovySyntax(expression);
        indicatorMap.put(name, expression);
    }

    @Override
    public String getIndicatorCode(String name) {
        return indicatorMap.get(name);
    }

    private void validateGroovySyntax(String scriptCode) throws IllegalArgumentException {
        // Kiểm tra đơn giản thoi
        GroovyShell shell = new GroovyShell();
        try {
            shell.parse(scriptCode);
        } catch (CompilationFailedException e) {
            throw new IllegalArgumentException("Lỗi cú pháp trong mã Groovy: " + e.getMessage(), e);
        }
    }
}
