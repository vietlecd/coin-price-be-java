package com.javaweb.helper;

import com.javaweb.DTO.FundingRateDTO;

public class FundingRateDTOHelper {

    public static FundingRateDTO createFundingRateDTO(String symbol, String fundingRate, String fundingCountdown, String time) {
        return new FundingRateDTO.Builder()
                .symbol(symbol)
                .fundingRate(fundingRate)
                .fundingCountdown(fundingCountdown)
                .time(time)
                .build();
    }
}
