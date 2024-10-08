package com.javaweb.service;

import java.util.List;

public interface IFutureWebSocketService {
    void connectToFutureWebSocket(List<String> streams);

    void closeWebSocket();
}
