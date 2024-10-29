package com.javaweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserIndicatorRequest {
    private String name;
    private String code;
}