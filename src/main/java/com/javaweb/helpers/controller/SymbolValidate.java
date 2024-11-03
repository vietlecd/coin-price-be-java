package com.javaweb.helpers.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

public class SymbolValidate {
    public static void validateSymbolsExist(List<String> symbols, Map<String, ? extends Object> dataMap, String type) {
        for (String symbol : symbols) {
            if (!dataMap.containsKey(type + " Price: " + symbol)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, type + " data for symbol " + symbol + " not found");
            }
        }
    }
}
