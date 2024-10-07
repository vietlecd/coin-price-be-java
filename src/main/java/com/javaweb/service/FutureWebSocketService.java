package com.javaweb.service;

import java.util.List;

public interface FutureWebSocketService {
    void connectToFutureWebSocket(List<String> streams);

    void closeWebSocket();
}
