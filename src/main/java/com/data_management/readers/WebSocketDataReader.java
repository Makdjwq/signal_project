package com.data_management.readers;

import com.data_management.DataStorage;
import com.data_management.MyWebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class is not currently used, however could be useful
 * Normally used as a websocket data reader, could connect disconnect ect.
 */
public class WebSocketDataReader implements WebSocketReader {
    private MyWebSocketClient client;
    private DataStorage dataStorage;

    public WebSocketDataReader(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {

    }

    @Override
    public void connect(String serverUri) {
        try {
            client = new MyWebSocketClient(new URI(serverUri), dataStorage);
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        if (client != null) {
            client.close();
        }
    }
}
