package com.javaweb.service;

import com.javaweb.dto.IndicatorDTO;

import java.util.List;
import java.util.Map;

public interface IIndicatorService {
    Map<String, IndicatorDTO> getIndicatorData(List<String> symbols, List<String> indicators, int days);
}
