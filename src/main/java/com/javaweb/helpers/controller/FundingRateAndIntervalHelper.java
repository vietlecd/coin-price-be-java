package com.javaweb.helpers.controller;

import com.javaweb.connect.impl.FundingIntervalWebService;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateAndIntervalDTO;
import com.javaweb.dto.FundingRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.javaweb.converter.FundingRateAndIntervalDTOHelper.combineData;

@Component
public class FundingRateAndIntervalHelper {

    public static void streamCombinedData(SseEmitter emitter, List<String> symbols,
                                          Map<String, FundingRateDTO> fundingRateDataMap,
                                          List<Map<String, FundingIntervalDTO>> fundingIntervalDataList) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            for (String symbol : symbols) {
                FundingRateDTO fundingRateDTO = fundingRateDataMap.get("FundingRate Price: " + symbol);

                for (Map<String, FundingIntervalDTO> fundingIntervalData : fundingIntervalDataList) {
                    FundingIntervalDTO fundingIntervalDTO = fundingIntervalData.get(symbol);

                    if (fundingRateDTO != null && fundingIntervalDTO != null) {
                        FundingRateAndIntervalDTO combinedDTO = combineData(fundingRateDTO, fundingIntervalDTO);
                        try {
                            emitter.send(SseEmitter.event().name("FundingRateAndInterval").data(combinedDTO));
                        } catch (Exception e) {
                            e.printStackTrace();
                            emitter.completeWithError(e);
                            scheduler.shutdown();
                        }
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);  // Stream every 1 second
    }
}
