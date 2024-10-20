package com.javaweb.service.stream;

import com.javaweb.config.WebSocketConfig;
import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateDTO;
import com.javaweb.helpers.controller.FundingRateAndIntervalHelper;
import com.javaweb.connect.impl.FundingIntervalWebService;
import com.javaweb.connect.impl.FundingRateWebSocketService;
import com.javaweb.service.impl.FundingRateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class FundingRateStreamService {

    @Autowired
    private FundingRateWebSocketService fundingRateWebSocketService;

    @Autowired
    private FundingRateDataService fundingRateDataService;

    @Autowired
    private FundingIntervalWebService fundingIntervalWebService;

    public SseEmitter handleStreamFundingRate(List<String> symbols) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        // Cập nhật định kỳ dữ liệu FundingInterval
        scheduleFundingIntervalDataUpdate(symbols);

        // Kết nối đến WebSocket cho FundingRate
        fundingRateWebSocketService.connectToWebSocket(symbols, false);

        // Tạo một luồng xử lý định kỳ để gửi dữ liệu SSE tới client
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Map<String, FundingRateDTO> fundingRateDataMap = fundingRateDataService.getFundingRateDataUsers();
            List<Map<String, FundingIntervalDTO>> fundingIntervalDataList = fundingIntervalWebService.getLatestFundingIntervalData(symbols);

            FundingRateAndIntervalHelper.streamCombinedData(sseEmitter, symbols, fundingRateDataMap, fundingIntervalDataList);

        }, 0, 5, TimeUnit.SECONDS); // Trigger mỗi 5s

        return sseEmitter;
    }

    // Hàm định kỳ để cập nhật dữ liệu FundingInterval
    public void scheduleFundingIntervalDataUpdate(List<String> symbols) {
        ScheduledExecutorService dataUpdater = Executors.newScheduledThreadPool(1);
        dataUpdater.scheduleAtFixedRate(() -> {
            fundingIntervalWebService.getLatestFundingIntervalData(symbols);
            System.out.println("FundingInterval data updated for symbols: " + symbols);
        }, 0, 15, TimeUnit.MINUTES); // Cập nhật mỗi 15 phút
    }
}
