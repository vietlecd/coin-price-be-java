package com.javaweb.service.trigger;

import com.javaweb.repository.FundingRateTriggerRepository;
import com.javaweb.repository.FuturePriceTriggerRepository;
import com.javaweb.repository.PriceDifferenceTriggerRepository;
import com.javaweb.repository.SpotPriceTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaweb.model.trigger.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TriggerSymbolService {
    @Autowired
    private PriceDifferenceTriggerRepository priceDifferenceTriggerRepository;

    @Autowired
    private SpotPriceTriggerRepository spotPriceTriggerRepository;

    @Autowired
    private FuturePriceTriggerRepository futurePriceTriggerRepository;

    @Autowired
    private FundingRateTriggerRepository fundingRateTriggerRepository;

    public Map<String, List<String>> getUsernamesWithSymbolsFundingRate() {
        List<FundingRateTrigger> triggers = fundingRateTriggerRepository.findAllUsernamesWithSymbols();

        return triggers.stream()
                .collect(Collectors.groupingBy(
                        FundingRateTrigger::getUsername,
                        Collectors.mapping(FundingRateTrigger::getSymbol, Collectors.toList())
                ));
    }

    public Map<String, List<String>> getUsernamesWithSymbolsSpotAndFuture() {
        List<PriceDifferenceTrigger> triggers = priceDifferenceTriggerRepository.findAllUsernamesWithSymbols();

        return triggers.stream()
                .collect(Collectors.groupingBy(
                        PriceDifferenceTrigger::getUsername,
                        Collectors.mapping(PriceDifferenceTrigger::getSymbol, Collectors.toList())
                ));
    }

    public Map<String, List<String>> getUsernamesWithSymbolsSpot() {
        List<SpotPriceTrigger> triggers = spotPriceTriggerRepository.findAllUsernamesWithSymbols();

        return triggers.stream()
                .collect(Collectors.groupingBy(
                        SpotPriceTrigger::getUsername,
                        Collectors.mapping(SpotPriceTrigger::getSymbol, Collectors.toList())
                ));
    }

    public Map<String, List<String>> getUsernamesWithSymbolsFuture() {
        List<FuturePriceTrigger> triggers = futurePriceTriggerRepository.findAllUsernamesWithSymbols();

        return triggers.stream()
                .collect(Collectors.groupingBy(
                        FuturePriceTrigger::getUsername,
                        Collectors.mapping(FuturePriceTrigger::getSymbol, Collectors.toList())
                ));
    }



    public List<String> getSpotSymbolsWithTriggers() {
        return spotPriceTriggerRepository.findAll().stream()
                .map(SpotPriceTrigger::getSymbol)
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
