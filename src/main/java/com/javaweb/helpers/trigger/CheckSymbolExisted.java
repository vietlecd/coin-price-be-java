package com.javaweb.helpers.trigger;

import com.javaweb.repository.FundingRateTriggerRepository;
import com.javaweb.repository.FuturePriceTriggerRepository;
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
    private TriggerMapHelper triggerMapHelper;

    public boolean symbolExistsInSpot(String symbol) {
        return spotPriceTriggerRepository.existsBySymbol(symbol);
    }

    public boolean symbolExistsInFuture(String symbol) {
        return futurePriceTriggerRepository.existsBySymbol(symbol);
    }

    public boolean symbolExistsInFundingRate(String symbol) {
        return fundingRateTriggerRepository.existsBySymbol(symbol);
    }
}
