package com.javaweb.service.impl;
import com.javaweb.model.mongo_entity.userIndicator;
import com.javaweb.repository.UserIndicatorRepository;
import com.javaweb.service.IUserIndicatorService;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserIndicatorService implements IUserIndicatorService {
    @Autowired
    private UserIndicatorRepository userIndicatorRepository;

    @Override
    public void addIndicator(userIndicator userIndicator) {
        validateGroovySyntax(userIndicator.getCode());
        userIndicatorRepository.save(userIndicator);
    }

    @Override
    public String getCode(String username, String name) {
        Optional<userIndicator> userIndicatorOptional = userIndicatorRepository.findByUsernameAndName(username, name);
        return userIndicatorOptional.map(userIndicator::getCode).orElse(null);
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
