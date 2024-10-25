package com.javaweb.service;

import com.javaweb.model.mongo_entity.userIndicator;

import java.util.Optional;

public interface IUserIndicatorService {
    Optional<userIndicator> findByUsernameAndName(String username, String name);
    void addIndicator(userIndicator userIndicator);
    String getCode(String username, String name);
}
