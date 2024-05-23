package com.data_management;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

/**
 * My implementation of WebSocketClient
 */
public class MyWebSocketClient extends WebSocketClient {
    private DataStorage dataStorage;

    /**
     * Constructor used if you want to use a draft
     * @param serverUri - server ulr
     * @param draft - draft
     * @param dataStorage - data storage used to store all data passed through messages
     */
    public MyWebSocketClient(URI serverUri, Draft draft, DataStorage dataStorage) {
        super(serverUri, draft);
        this.dataStorage = dataStorage;
    }
    /**
     * Constructor used to create WebSocketClient with a dataStorage
     * @param serverURI - server ulr
     * @param dataStorage - data storage used to store all data passed through messages
     */
    public MyWebSocketClient(URI serverURI, DataStorage dataStorage) {
        super(serverURI);
        this.dataStorage = dataStorage;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Hello, it is me. Mario :)"); // Copied from the given website haha
        System.out.println("new connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        try {
            // Parse the message and store the data
            String[] parts = message.split(",");
            if (parts.length != 4) {
                throw new IllegalArgumentException("Invalid message format");
            }
            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String recordType = parts[2];
            double measurementValue = Double.parseDouble(parts[3]);

            // Use the dataStorage instance to store the data
            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);

        }
        // Here I decided to catch and log the incorrect messages, instead of adding
        // faulty message to the dataStorage
        catch (Exception e) {
            System.err.println("Error processing message: " + message);
            e.printStackTrace();
        }
    }
    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("Received ByteBuffer message");
        // You can implement handling for binary messages if needed
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

    /**
     * Not really useful for now, but for debugging purposes
     * @param args - args
     */
    public static void main(String[] args) {
        try {
            DataStorage dataStorage = new DataStorage();
            WebSocketClient client = new MyWebSocketClient(new URI("ws://localhost:8887"), dataStorage);
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
