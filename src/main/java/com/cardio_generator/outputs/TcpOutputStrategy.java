package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * Implements the {@link OutputStrategy} interface to send health data over a TCP connection.
 * This class sets up a TCP server that listens on a specified port and waits for a client to connect.
 * Once connected, it allows sending formatted health data to the connected client.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Initializes a TCP server on the specified port and waits for a client connection.
     * Data sending capabilities are enabled once a client connects.
     *
     * @param port the TCP port on which the server will listen for client connections.
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends formatted health data to the connected client over TCP.
     * If no client is connected, the data is not sent.
     *
     * @param patientId The unique identifier of the patient for whom the data is generated.
     * @param timestamp The time at which the data was generated, represented as a long timestamp.
     * @param label A descriptive label for the type of data being output, such as "Heart Rate" or "Blood Pressure".
     *              This label helps in categorizing and processing the output data correctly.
     * @param data The actual health data generated for the patient, formatted as a string.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
