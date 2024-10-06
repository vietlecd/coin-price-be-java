package com.javaweb.helper;

import com.javaweb.model.FundingRateDTO;

public class FundingRateDTOHelper {

    public static FundingRateDTO createFundingRateDTO(String symbol, String fundingRate, String countdownFormatted, String intervalFormatted, String time) {
        return new FundingRateDTO.Builder()
                .symbol(symbol)
                .fundingRate(fundingRate)
                .countdownFormatted(countdownFormatted)
                .intervalFormatted(intervalFormatted)
                .time(time)
                .build();
    }
}
