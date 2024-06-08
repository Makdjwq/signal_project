package com.data_management.readers;

import com.data_management.DataStorage;

import java.io.IOException;

public interface WebSocketReader {

    /**
     * Reads data from a specified source and stores it in the data storage.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;

    /**
     * Used to connect to the server
     * @param serverUri - server url
     */
    void connect(String serverUri);
    /**
     * Used to disconnect to the server
     */
    void disconnect();


}
