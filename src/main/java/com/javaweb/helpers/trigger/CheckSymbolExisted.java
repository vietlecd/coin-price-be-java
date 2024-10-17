package com.javaweb.helpers.trigger;

import com.javaweb.repository.FundingRateTriggerRepository;
import com.javaweb.repository.FuturePriceTriggerRepository;
import com.javaweb.repository.PriceDifferenceTriggerRepository;
import com.javaweb.repository.SpotPriceTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckSymbolExisted {
    @Autowired
    private SpotPriceTriggerRepository spotPriceTriggerRepository;

    @Autowired
    private FuturePriceTriggerRepository futurePriceTriggerRepository;

    @Autowired
    private FundingRateTriggerRepository fundingRateTriggerRepository;

    @Autowired
    private PriceDifferenceTriggerRepository priceDifferenceTriggerRepository;

    @Autowired
    private TriggerMapHelper triggerMapHelper;

    public boolean symbolExistsInSpot(String symbol, String username) {
        return spotPriceTriggerRepository.existsBySymbolAndUsername(symbol, username);
    }

    public boolean symbolExistsInFuture(String symbol, String username) {
        return futurePriceTriggerRepository.existsBySymbolAndUsername(symbol, username);
    }

    public boolean symbolExistsInFundingRate(String symbol, String username) {
        return fundingRateTriggerRepository.existsBySymbolAndUsername(symbol, username);
    }

    public boolean symbolExistsInPriceDifference(String symbol, String username) {
        return priceDifferenceTriggerRepository.existsBySymbolAndUsername(symbol, username);
    }
}
