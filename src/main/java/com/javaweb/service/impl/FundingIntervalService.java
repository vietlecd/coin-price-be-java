package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaweb.DTO.FundingIntervalDTO;
import com.javaweb.service.IFundingIntervalService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FundingIntervalService implements IFundingIntervalService {
    private final RestTemplate restTemplate = new RestTemplate();

    // API từ CoinGecko
    private static final String FUNDINGINTERVAL_API_URL = "https://fapi.binance.com/fapi/v1/fundingInfo";

    public List<Map<String, FundingIntervalDTO>> getFundingIntervalData(List<String> symbols) {
        List<Map<String, FundingIntervalDTO>> fundingIntervalDataList = new ArrayList<>();

        for (String symbol : symbols) {
            String url = FUNDINGINTERVAL_API_URL + symbol;
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null && response.isArray() && response.size() > 0) {
                JsonNode fundingIntervalData = response.get(0);
                symbol = fundingIntervalData.get("symbol").asText();
                String adjustedFundingRateCap = fundingIntervalData.get("adjustedFundingRateCap").asText();
                String adjustedFundingRateFloor = fundingIntervalData.get("adjustedFundingRateFloor").asText();
                Long fundingIntervalHours = fundingIntervalData.get("fundingIntervalHours").asLong();
            }
        }

        // Trả về danh sách dữ liệu thị trường dưới dạng JSON
        return fundingIntervalDataList;
    }
}
