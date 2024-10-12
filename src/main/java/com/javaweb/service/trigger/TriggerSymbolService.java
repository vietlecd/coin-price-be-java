package com.javaweb.service.trigger;

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

    public List<String> getAllSymbolsWithTriggers() {
        // Lấy tất cả các symbols đã có trigger từ cả Spot và Future trigger
        List<String> spotSymbols = spotPriceTriggerRepository.findAll().stream()
                .map(SpotPriceTrigger::getSymbol)
                .collect(Collectors.toList());

        List<String> futureSymbols = futurePriceTriggerRepository.findAll().stream()
                .map(FuturePriceTrigger::getSymbol)
                .collect(Collectors.toList());

        spotSymbols.addAll(futureSymbols);
        return spotSymbols;
    }
}
