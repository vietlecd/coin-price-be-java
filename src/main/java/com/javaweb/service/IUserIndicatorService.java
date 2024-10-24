package com.javaweb.service;

public interface IUserIndicatorService {
    void addIndicator(String name, String code);
    String getIndicatorCode(String name);
}
