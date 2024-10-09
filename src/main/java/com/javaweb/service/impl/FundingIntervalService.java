//package com.javaweb.service.impl;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.javaweb.DTO.FundingIntervalDTO;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class FundingIntervalService {
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    // API từ CoinGecko
//    private static final String FUNDINGINTERVAL_API_URL = "https://fapi.binance.com/fapi/v1/fundingInfo";
//
//    public List<Map<String, FundingIntervalDTO>> getFundingIntervalData(List<String> symbols) {
//        List<Map<String, FundingIntervalDTO>> fundingIntervalDataList = new ArrayList<>();
//
//        for (String symbol : symbols) {
//            String url = FUNDINGINTERVAL_API_URL + symbol;
//            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
//
//            if (response != null && response.isArray() && response.size() > 0) {
//                JsonNode fundingIntervalData = response.get(0);
//                double marketCap = fundingIntervalData.get("market_cap").asDouble();
//                double tradingVolume = fundingIntervalData.get("total_volume").asDouble();
//
//                // Tạo một Map chứa dữ liệu của symbol hiện tại
////                Map<String, Object> symbolData = new HashMap<>();
////                symbolData.put("symbol", symbol);
////                symbolData.put("marketCap", marketCap);
////                symbolData.put("tradingVolume", tradingVolume);
//
//                // Thêm vào danh sách kết quả
//                fundingIntervalDataList.add(symbolData);
//            }
//        }
//
//        // Trả về danh sách dữ liệu thị trường dưới dạng JSON
//        return fundingIntervalDataList;
//    }
//}
