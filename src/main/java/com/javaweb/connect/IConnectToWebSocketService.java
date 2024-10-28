package com.javaweb.connect;

import com.javaweb.dto.FundingIntervalDTO;
import com.javaweb.dto.FundingRateDTO;

import java.util.List;
import java.util.Map;

public interface IConnectToWebSocketService {
    // Connect to WebSocket for Funding Rate
    void connectToWebSocket(List<String> streams, boolean isTriggerRequest);

//    void closeWebSocket();
}
