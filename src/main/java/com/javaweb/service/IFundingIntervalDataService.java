package com.javaweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.DTO.FundingIntervalDTO;

import java.util.List;
import java.util.Map;

public interface IFundingIntervalDataService {
    Map<String, FundingIntervalDTO> processFundingIntervalData(JsonNode fundingIntervalData);
}
