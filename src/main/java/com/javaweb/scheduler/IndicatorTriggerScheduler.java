package com.javaweb.scheduler;

import com.javaweb.service.trigger.TriggerService;
import com.javaweb.service.trigger.TriggerSymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class IndicatorTriggerScheduler {
    @Autowired
    private TriggerSymbolService triggerSymbolService;

    @Autowired
    private TriggerService triggerService;

    @Scheduled(fixedRate = 300000) //
    public void sched() {
        Map<String, List<String>> usernamesWithSymbolsForIndicator = triggerSymbolService.getUsernamesWithSymbolsIndicator();
        if (!usernamesWithSymbolsForIndicator.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : usernamesWithSymbolsForIndicator.entrySet()) {
                String username = entry.getKey();
                List<String> symbols = entry.getValue();

                triggerService.handleAndSendAlertForIndicator(symbols, username);
            }
        }
    }
}
