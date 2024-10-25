package com.javaweb.service.trigger;

import org.springframework.stereotype.Service;

@Service
public class BinanceTokenListingService {

    public void handleNewToken(String message) {
        // Xử lý logic khi nhận thông báo token mới từ WebSocket
        System.out.println("Token mới niêm yết: " + message);
        // Thêm logic để lưu trữ hoặc thông báo token mới tại đây
    }
}
