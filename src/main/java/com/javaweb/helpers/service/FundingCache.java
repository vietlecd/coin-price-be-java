package com.javaweb.helpers.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.javaweb.dto.FundingIntervalDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class FundingCache {

    private final Cache<String, List<Map<String, FundingIntervalDTO>>> cache;

    // Khởi tạo cache với thời gian hết hạn là 1 giờ
    public FundingCache() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)  // Dữ liệu tự động hết hạn sau 1 giờ
                .build();
    }

    // Lưu dữ liệu vào cache
    public void put(String key, List<Map<String, FundingIntervalDTO>> data) {
        cache.put(key, data);
    }

    // Lấy dữ liệu từ cache
    public List<Map<String, FundingIntervalDTO>> get(String key) {
        return cache.getIfPresent(key);
    }

    // Xóa cache khi cần thiết
    public void invalidate(String key) {
        cache.invalidate(key);
    }
}

