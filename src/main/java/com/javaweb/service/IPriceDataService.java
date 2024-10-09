package com.javaweb.service;

import com.javaweb.DTO.FundingIntervalDTO;
import com.javaweb.DTO.FundingRateDTO;
import com.javaweb.DTO.PriceDTO;

import java.util.Map;

public interface IPriceDataService {
    void updatePriceData(PriceDTO priceDTO);

    void updateFundingRate(FundingRateDTO fundingRateDTO);

    void updateFundingInterval(FundingIntervalDTO fundingIntervalDTO);

    Map<String, PriceDTO> getPriceDataMap();

    Map<String, FundingRateDTO> getFundingRateDataMap();

    Map<String, FundingIntervalDTO> getFundingIntervalDataMap();
}
