package com.javaweb.service.trigger;

import com.javaweb.model.trigger.FundingRateTrigger;
import com.javaweb.model.trigger.FuturePriceTrigger;
import com.javaweb.model.trigger.PriceDifferenceTrigger;
import com.javaweb.model.trigger.SpotPriceTrigger;
import com.javaweb.repository.trigger.FundingRateTriggerRepository;
import com.javaweb.repository.trigger.FuturePriceTriggerRepository;
import com.javaweb.repository.trigger.PriceDifferenceTriggerRepository;
import com.javaweb.repository.trigger.SpotPriceTriggerRepository;
import com.javaweb.model.trigger.*;
import com.javaweb.repository.trigger.IndicatorTriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTriggerService {
    @Autowired
    private IndicatorTriggerRepository indicatorTriggerRepository;

    @Autowired
    private SpotPriceTriggerRepository spotPriceTriggerRepository;

    @Autowired
    private FuturePriceTriggerRepository futurePriceTriggerRepository;

    @Autowired
    private PriceDifferenceTriggerRepository priceDifferenceTriggerRepository;

    @Autowired
    private FundingRateTriggerRepository fundingRateTriggerRepository;
    public List<Object> findAllTriggersByUsername(String username) {
        // Lấy dữ liệu từ tất cả repository theo username
        List<SpotPriceTrigger> spotTriggers = spotPriceTriggerRepository.findByUsername(username);
        List<FuturePriceTrigger> futureTriggers = futurePriceTriggerRepository.findByUsername(username);
        List<PriceDifferenceTrigger> priceDifferenceTriggers = priceDifferenceTriggerRepository.findByUsername(username);
        List<FundingRateTrigger> fundingRateTriggers = fundingRateTriggerRepository.findByUsername(username);
        List<IndicatorTrigger> indicatorTriggers = indicatorTriggerRepository.findByUsername(username);

        // Tạo một danh sách chung để trả về tất cả dữ liệu
        List<Object> allTriggers = new ArrayList<>();
        allTriggers.addAll(spotTriggers);
        allTriggers.addAll(futureTriggers);
        allTriggers.addAll(priceDifferenceTriggers);
        allTriggers.addAll(fundingRateTriggers);
        allTriggers.addAll(indicatorTriggers);

        return allTriggers;
    }
}

