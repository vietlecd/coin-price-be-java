package com.javaweb.connect;

import java.util.List;

public interface ISpotWebSocketService {

    void connectToSpotWebSocket(List<String> streams);

    void closeWebSocket();
}
