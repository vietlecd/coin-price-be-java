package com.javaweb.helpers;

import com.javaweb.DTO.FundingIntervalDTO;

public class FundingIntervalDTOHelper {
    public static FundingIntervalDTO createFundingRateDTO(String symbol, String adjustedFundingRateCap, String adjustedFundingRateFloor, Long fundingIntervalHours) {
        return new FundingIntervalDTO.Builder()
                .symbol(symbol)
                .adjustedFundingRateCap(adjustedFundingRateCap)
                .adjustedFundingRateFloor(adjustedFundingRateFloor)
                .fundingIntervalHours(fundingIntervalHours)
                .build();
    }
}
