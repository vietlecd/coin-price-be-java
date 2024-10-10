package com.javaweb.connect.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.connect.IFundingIntervalWebService;
import com.javaweb.service.impl.FundingIntervalDataService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    // API từ Binance
    private static final String FUNDINGINTERVAL_API_URL = "https://fapi.binance.com/fapi/v1/fundingInfo?symbol=";

    @Override
    public List<Map<String, FundingIntervalDTO>> handleFundingIntervalWeb(List<String> symbols) {
        List<Map<String, FundingIntervalDTO>> fundingIntervalDataList = new ArrayList<>();

        for (String symbol : symbols) {
            // Cấu trúc URL với query parameter ?symbol=
            String url = FUNDINGINTERVAL_API_URL + symbol;

            // Gửi yêu cầu đến API và nhận phản hồi
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null && response.isArray()) {
                // Duyệt qua mảng JSON và lọc theo symbol
                for (JsonNode fundingInfo : response) {
                    if (fundingInfo.get("symbol").asText().equals(symbol)) {
                        // In trực tiếp JSON cho symbol được lọc
                        System.out.println("Received JSON for symbol " + symbol + ": " + fundingInfo.toString());

                        // Xử lý dữ liệu từ JSON node
                        Map<String, FundingIntervalDTO> processedData = fundingIntervalDataService.processFundingIntervalData(fundingInfo);

                        // Thêm kết quả xử lý vào danh sách
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
