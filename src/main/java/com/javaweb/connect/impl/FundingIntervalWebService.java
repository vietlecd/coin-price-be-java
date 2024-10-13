package com.javaweb.connect.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.connect.IFundingIntervalWebService;
import com.javaweb.service.impl.FundingIntervalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FundingIntervalWebService implements IFundingIntervalWebService {

    @Autowired
    private FundingIntervalDataService fundingIntervalDataService;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String FUNDINGINTERVAL_API_URL = "https://fapi.binance.com/fapi/v1/fundingInfo?symbol=";

    @Override
    public List<Map<String, FundingIntervalDTO>> handleFundingIntervalWeb(List<String> symbols) {
        List<Map<String, FundingIntervalDTO>> fundingIntervalDataList = new ArrayList<>();

        for (String symbol : symbols) {
            String url = FUNDINGINTERVAL_API_URL + symbol;
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null && response.isArray()) {
                for (JsonNode fundingInfo : response) {
                    if (fundingInfo.get("symbol").asText().equals(symbol)) {

                        // Xử lý dữ liệu từ JSON node
                        Map<String, FundingIntervalDTO> processedData = fundingIntervalDataService.processFundingIntervalData(fundingInfo);

                        fundingIntervalDataList.add(processedData);
                    }
                }
            } else {
                System.out.println("No valid data received for symbol: " + symbol);
            }
        }

        return fundingIntervalDataList;
    }
}
