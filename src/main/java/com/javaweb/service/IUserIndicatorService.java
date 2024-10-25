package com.javaweb.service;

import com.javaweb.model.mongo_entity.userIndicator;

public interface IUserIndicatorService {
    void addIndicator(userIndicator userIndicator);
    String getCode(String username, String name);
}
