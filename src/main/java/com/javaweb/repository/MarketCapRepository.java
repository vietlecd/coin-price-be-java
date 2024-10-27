//package com.javaweb.repository;
//
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
//import com.javaweb.model.MarketCapResponse;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.concurrent.TimeUnit;
//
//@Repository
//public class MarketCapRepository {
//
//    private final Cache<String, MarketCapResponse> cache;
//    private final RestTemplate restTemplate;
//
//    public MarketCapRepository() {
//        // Khởi tạo bộ nhớ đệm với thời gian hết hạn là 15 phút
//        this.cache = Caffeine.newBuilder()
//                .expireAfterWrite(15, TimeUnit.MINUTES)
//                .maximumSize(100)
//                .build();
//
//        // Khởi tạo RestTemplate để thực hiện các yêu cầu HTTP đồng bộ
//        this.restTemplate = new RestTemplate();
//    }
//
//    // Phương thức mới fetch dữ liệu theo 'symbol'
//    public MarketCapResponse fetchMarketCapBySymbol(String symbol) {
//        // Kiểm tra cache với 'symbol'
//        MarketCapResponse cachedMarketCap = cache.getIfPresent(symbol);
//        if (cachedMarketCap != null) {
//            return cachedMarketCap;
//        }
//
//        // Nếu không có trong cache, gọi API cho 'symbol' cụ thể
//        String url = String.format("https://api.coingecko.com/api/v3/coins/%s", symbol);
//        MarketCapResponse marketCapResponse = restTemplate.getForObject(url, MarketCapResponse.class);
//
//        // Lưu vào cache với 'symbol'
//        cache.put(symbol, marketCapResponse);
//
//        // Xử lý dữ liệu từ API: Lấy giá trị market cap và volume
//        double marketCap = marketCapResponse.getMarket_data().getMarket_cap().getUsd();
//        double totalVolume = marketCapResponse.getMarket_data().getTotal_volume().getUsd();
//
//        System.out.println("MarketCap USD: " + marketCap);
//        System.out.println("Total Volume USD: " + totalVolume);
//
//        return marketCapResponse;
//    }
//}
