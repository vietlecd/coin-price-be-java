package com.javaweb.service.trigger;

import com.javaweb.model.trigger.*;
import com.javaweb.repository.trigger.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private IndicatorTriggerRepository indicatorTriggerRepository;

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

    public Map<String, List<String>> getUsernamesWithSymbolsIndicator() {
        List<IndicatorTrigger> triggers = indicatorTriggerRepository.findAllUsernamesWithSymbols();

        return triggers.stream()
                .collect(Collectors.groupingBy(
                        IndicatorTrigger::getUsername,
                        Collectors.mapping(IndicatorTrigger::getSymbol, Collectors.toList())
                ));
    }
}
