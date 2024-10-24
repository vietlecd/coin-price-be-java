package com.javaweb.converter;

import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateAndIntervalDTO;
import com.javaweb.dto.FundingRateDTO;

public class FundingRateAndIntervalDTOHelper {
    public static FundingRateAndIntervalDTO combineData(FundingRateDTO fundingRateDTO, FundingIntervalDTO fundingIntervalDTO) {
        return new FundingRateAndIntervalDTO(
                fundingRateDTO.getSymbol(),
                fundingRateDTO.getFundingRate(),
                fundingRateDTO.getFundingCountdown(),
                fundingRateDTO.getEventTime(),
                fundingIntervalDTO != null ? fundingIntervalDTO.getAdjustedFundingRateCap() : "0",
                fundingIntervalDTO != null ? fundingIntervalDTO.getAdjustedFundingRateFloor() : "0",
                fundingIntervalDTO != null ? fundingIntervalDTO.getFundingIntervalHours() : 0
        );
    }
}
