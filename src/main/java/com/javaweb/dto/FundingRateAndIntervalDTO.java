package com.javaweb.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(builder = FundingRateDTO.Builder.class)
public class FundingRateAndIntervalDTO {
    private String symbol;
    private String fundingRate;
    private String fundingCountdown;
    private String eventTime;
    private String adjustedFundingRateCap;
    private String adjustedFundingRateFloor;
    private Long fundingIntervalHours;

}
