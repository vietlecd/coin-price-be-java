package com.javaweb.connect;

import com.javaweb.dto.FundingIntervalDTO;

import java.util.List;
import java.util.Map;

public interface IFundingIntervalWebService {
    List<Map<String, FundingIntervalDTO>> getLatestFundingIntervalData(List<String> symbols);

    List<Map<String, FundingIntervalDTO>> handleFundingIntervalWeb(List<String> symbols);
}
