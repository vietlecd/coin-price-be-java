package com.javaweb.helpers;

import com.javaweb.DTO.FundingRateDTO;

public class FundingRateDTOHelper {

    public static FundingRateDTO createFundingRateDTO(String symbol, String fundingRate, String fundingCountdown, String eventTime) {
        return new FundingRateDTO.Builder()
                .symbol(symbol)
                .fundingRate(fundingRate)
                .fundingCountdown(fundingCountdown)
                .eventTime(eventTime)
                .build();
    }
}
