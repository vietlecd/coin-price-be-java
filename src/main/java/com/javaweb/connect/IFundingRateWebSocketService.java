package com.javaweb.connect;

import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateDTO;

import java.util.List;
import java.util.Map;

public interface IFundingRateWebSocketService {
    // Connect to WebSocket for Funding Rate
    void connectToFundingRateWebSocket(List<String> streams);

//    void closeWebSocket();
}
