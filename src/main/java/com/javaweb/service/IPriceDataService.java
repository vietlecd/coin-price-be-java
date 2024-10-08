package com.javaweb.service;

import com.javaweb.model.FundingRateDTO;
import com.javaweb.model.PriceDTO;

import java.util.Map;

public interface IPriceDataService {
    void updatePriceData(PriceDTO priceDTO);

    void updateFundingRate(FundingRateDTO fundingRateDTO);

    Map<String, PriceDTO> getPriceDataMap();

    Map<String, FundingRateDTO> getFundingRateDataMap();
}
