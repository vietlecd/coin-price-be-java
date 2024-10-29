package com.javaweb.connect;

import com.javaweb.dto.FundingIntervalDTO;

import java.util.List;
import java.util.Map;

public interface IFundingIntervalWebService {
    Map<String, FundingIntervalDTO> getLatestFundingIntervalData(List<String> symbols);

    Map<String, FundingIntervalDTO> handleFundingIntervalWeb(List<String> symbols);
}
