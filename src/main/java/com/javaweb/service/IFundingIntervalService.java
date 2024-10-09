package com.javaweb.service;

import com.javaweb.DTO.FundingIntervalDTO;

import java.util.List;
import java.util.Map;

public interface IFundingIntervalService {
    List<Map<String, FundingIntervalDTO>> getFundingIntervalData(List<String> symbols);
}
