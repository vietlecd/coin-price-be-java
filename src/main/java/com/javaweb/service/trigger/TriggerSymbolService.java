package com.javaweb.service.trigger;

import com.javaweb.repository.FundingRateTriggerRepository;
import com.javaweb.repository.FuturePriceTriggerRepository;
import com.javaweb.repository.SpotPriceTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaweb.model.trigger.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TriggerSymbolService {

    @Autowired
    private SpotPriceTriggerRepository spotPriceTriggerRepository;

    @Autowired
    private FuturePriceTriggerRepository futurePriceTriggerRepository;

    @Autowired
    private FundingRateTriggerRepository fundingRateTriggerRepository;

    public List<String> getSpotSymbolsWithTriggers() {
        return spotPriceTriggerRepository.findAll().stream()
                .map(SpotPriceTrigger :: getSymbol)
                .collect(Collectors.toList());
    }

    public List<String> getFutureSymbolsWithTriggers() {
        return futurePriceTriggerRepository.findAll().stream()
                .map(FuturePriceTrigger::getSymbol)
                .collect(Collectors.toList());
    }

    public List<String> getFundingRateSymbolsWithTriggers() {
        return fundingRateTriggerRepository.findAll().stream()
                .map(FundingRateTrigger::getSymbol)
                .collect(Collectors.toList());
    }
}
