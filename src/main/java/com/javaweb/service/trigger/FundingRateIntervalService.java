package com.javaweb.service.trigger;

import com.javaweb.model.trigger.FundingRateInterval;
import com.javaweb.model.trigger.UserTradingPair;
import com.javaweb.repository.FundingRateIntervalRepository;
import com.javaweb.repository.UserTradingPairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class FundingRateIntervalService {

    @Autowired
    private FundingRateIntervalRepository fundingRateIntervalRepository;

    @Autowired
    private UserTradingPairRepository userTradingPairRepository;

    // Lấy các cặp giao dịch người dùng đã chọn
    public List<String> getUserSelectedTradingPairs(String userId) {
        UserTradingPair userTradingPair = userTradingPairRepository.findByUserId(userId);
        if (userTradingPair != null) {
            return userTradingPair.getTradingPairs();
        }
        // Trả về danh sách rỗng nếu người dùng không có cặp giao dịch
        return Collections.emptyList();  
    }

    public FundingRateInterval fetchLatestFundingRateInterval(String symbol) {
        String apiUrl = "https://fapi.binance.com/fapi/v1/fundingRate?symbol=" + symbol + "&limit=1";
        RestTemplate restTemplate = new RestTemplate();
        FundingRateInterval[] fundingRates = restTemplate.getForObject(apiUrl, FundingRateInterval[].class);

        if (fundingRates != null && fundingRates.length > 0) {
            FundingRateInterval newInterval = new FundingRateInterval();
            newInterval.setSymbol(fundingRates[0].getSymbol());
            newInterval.setFundingTime(fundingRates[0].getFundingTime());
            newInterval.setNextFundingTime(fundingRates[0].getNextFundingTime());
            return newInterval;
        }
        return null;
    }

    public boolean hasFundingRateIntervalChanged(FundingRateInterval newInterval) {
        FundingRateInterval lastInterval = fundingRateIntervalRepository.findTopBySymbolOrderByTimestampDesc(newInterval.getSymbol());
        return lastInterval == null || lastInterval.getIntervalTime() != newInterval.getIntervalTime();
    }

    public void saveFundingRateInterval(FundingRateInterval interval) {
        fundingRateIntervalRepository.save(interval);
    }
}
