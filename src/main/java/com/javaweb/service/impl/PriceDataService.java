package com.javaweb.service.impl;

import com.javaweb.DTO.FundingIntervalDTO;
import com.javaweb.DTO.FundingRateDTO;
import com.javaweb.DTO.PriceDTO;
import com.javaweb.service.IPriceDataService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceDataService implements IPriceDataService {
    private final Map<String, PriceDTO> priceDataMap = new ConcurrentHashMap<>();
    private final Map<String, FundingRateDTO> fundingRateDataMap = new ConcurrentHashMap<>();
    private final Map<String, FundingIntervalDTO> fundingIntervalDataMap = new ConcurrentHashMap<String, FundingIntervalDTO>();

    public void updatePriceData(PriceDTO priceDTO) {
        priceDataMap.put(priceDTO.getSymbol(), priceDTO);
    }

    public Map<String, PriceDTO> getPriceDataMap() {
        return priceDataMap;
    }

    public void updateFundingRate(FundingRateDTO fundingRateDTO) {
        fundingRateDataMap.put(fundingRateDTO.getSymbol(), fundingRateDTO);
    }

    public Map<String, FundingRateDTO> getFundingRateDataMap() {
        return fundingRateDataMap;
    }


    public void updateFundingInterval(FundingIntervalDTO fundingIntervalDTO) {
        fundingIntervalDataMap.put(fundingIntervalDTO.getSymbol(), fundingIntervalDTO);
    }

    public Map<String, FundingIntervalDTO> getFundingIntervalDataMap() {
        return fundingIntervalDataMap;
    }
}
