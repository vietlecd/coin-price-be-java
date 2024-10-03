package com.javaweb.service;

import java.util.List;

public interface BinanceWebSocketService {
    void connectToWebSocket(List<String> streams);

    void closeWebSocket();
}
