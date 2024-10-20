package com.javaweb.service.trigger;

import com.javaweb.dto.trigger.FundingRateTriggerDTO;
import com.javaweb.dto.trigger.FuturePriceTriggerDTO;
import com.javaweb.dto.trigger.PriceDifferenceTriggerDTO;
import com.javaweb.dto.trigger.SpotPriceTriggerDTO;
import com.javaweb.model.trigger.*;
import com.javaweb.repository.*;
import com.javaweb.service.trigger.CRUD.FundingRateTriggerService;
import com.javaweb.service.trigger.CRUD.FuturePriceTriggerService;
import com.javaweb.service.trigger.CRUD.PriceDifferenceTriggerService;
import com.javaweb.service.trigger.CRUD.SpotPriceTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<IndicatorTrigger> indicatorTriggers = indicatorTriggerRepository.findByUsername(username);
        List<SpotPriceTrigger> spotTriggers = spotPriceTriggerRepository.findByUsername(username);
        List<FuturePriceTrigger> futureTriggers = futurePriceTriggerRepository.findByUsername(username);
        List<PriceDifferenceTrigger> priceDifferenceTriggers = priceDifferenceTriggerRepository.findByUsername(username);
        List<FundingRateTrigger> fundingRateTriggers = fundingRateTriggerRepository.findByUsername(username);

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