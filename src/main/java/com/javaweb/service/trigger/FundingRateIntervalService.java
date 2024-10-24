package com.javaweb.service.trigger;

import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.model.trigger.FundingRateInterval;
import com.javaweb.model.trigger.UserTradingPair;
import com.javaweb.repository.FundingRateIntervalRepository;
import com.javaweb.repository.UserTradingPairRepository; // Giả sử bạn có repository này
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FundingRateIntervalService {

    @Autowired
    private FundingRateIntervalRepository fundingRateIntervalRepository;

    @Autowired
    private UserTradingPairRepository userTradingPairRepository; // Thêm repository cho UserTradingPair

    // Fetch the latest funding rate interval from Binance API
    public FundingIntervalDTO fetchLatestFundingRateInterval(String symbol) {
        String apiUrl = "https://fapi.binance.com/fapi/v1/fundingRate?symbol=" + symbol + "&limit=1";
        RestTemplate restTemplate = new RestTemplate();
        FundingRateInterval[] fundingRates = restTemplate.getForObject(apiUrl, FundingRateInterval[].class);

        if (fundingRates != null && fundingRates.length > 0) {
            return new FundingIntervalDTO.Builder()
                    .symbol(fundingRates[0].getSymbol())
                    .fundingIntervalHours(calculateInterval(fundingRates[0].getFundingTime(), fundingRates[0].getNextFundingTime()))
                    .build();
        }
        return null;
    }

    // Helper method to calculate funding interval in hours
    private Long calculateInterval(long fundingTime, long nextFundingTime) {
        return (nextFundingTime - fundingTime) / (1000 * 60 * 60);  // Convert milliseconds to hours
    }

    // Check if funding rate interval has changed
    public boolean hasFundingRateIntervalChanged(FundingIntervalDTO newInterval) {
        FundingRateInterval lastInterval = fundingRateIntervalRepository.findTopBySymbolOrderByTimestampDesc(newInterval.getSymbol());
        return lastInterval == null || lastInterval.getIntervalTime() != newInterval.getFundingIntervalHours(); 
    }

    // Convert FundingIntervalDTO to FundingRateInterval
    private FundingRateInterval convertToEntity(FundingIntervalDTO dto) {
        FundingRateInterval interval = new FundingRateInterval();
        interval.setSymbol(dto.getSymbol());
        interval.setFundingTime(System.currentTimeMillis()); // Set funding time as current time
        interval.setNextFundingTime(interval.getFundingTime() + (dto.getFundingIntervalHours() * 3600000)); // Calculate next funding time
        interval.setTimestamp(LocalDateTime.now()); // Add timestamp
        return interval;
    }

    // Save funding rate interval
    public void saveFundingRateInterval(FundingIntervalDTO intervalDTO) {
        FundingRateInterval interval = convertToEntity(intervalDTO);
        fundingRateIntervalRepository.save(interval);
    }

    public void createFundingIntervalTrigger(FundingIntervalDTO fundingIntervalDTO) {
        // Implement trigger creation logic if needed
    }

    public void deleteFundingRateInterval(String symbol) {
        // Implement delete logic
    }

    public List<String> getUserSelectedTradingPairs(String userId) {
        UserTradingPair userTradingPair = userTradingPairRepository.findByUserId(userId);
        if (userTradingPair != null) {
            return userTradingPair.getTradingPairs();
        }
        return Collections.emptyList();  // Use Collections.emptyList() for an empty list
    }
}
