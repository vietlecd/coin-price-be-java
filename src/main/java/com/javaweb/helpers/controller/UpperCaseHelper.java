package com.javaweb.helpers.controller;

import java.util.ArrayList;
import java.util.List;

public class UpperCaseHelper {
    public static List<String> converttoUpperCase(List<String> symbols) {
        List<String> upperCaseSymbols = new ArrayList<>();
        for (String symbol : symbols) {
            upperCaseSymbols.add(symbol.toUpperCase());
        }
        return upperCaseSymbols;
    }
}

