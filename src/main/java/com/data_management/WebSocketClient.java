package com.data_management;

import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * WebSocketClient is a client for connecting to a WebSocket server.
 * It handles connection, message reception, and error handling.
 */
public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    private final DataStorage storage;

    /**
     * Constructor to initialize the WebSocket client with the server URI and data storage.
     *
     * @param serverURI the URI of the WebSocket server
     * @param dataStorage the DataStorage instance for storing received data
     * @throws URISyntaxException if the server URI is invalid
     */
    public WebSocketClient(URI serverURI, DataStorage dataStorage) throws URISyntaxException {
        super(serverURI);
        storage = dataStorage;
        System.out.println("Client created successfully");
    }

    /**
     * Called when the WebSocket connection is opened.
     *
     * @param handshakedata the handshake data from the server
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Client open");
        System.out.println("Connected to WebSocket server");
    }

    /**
     * Called when a message is received from the server.
     *
     * @param message the received message
     */
    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        try {
            // Parse the message and store the data
            String[] parts = message.split(",");
            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String recordType = parts[2];
            double measurementValue;

            // Check if the value contains a percentage sign
            if (parts[3].contains("%")) {
                measurementValue = Double.parseDouble(parts[3].substring(0, parts[3].length() - 1));
            } else {
                measurementValue = Double.parseDouble(parts[3]);
            }

            // Use the dataStorage instance to store the data
            storage.addPatientData(patientId, measurementValue, recordType, timestamp);
        } catch (Exception e) {
            // Handle any errors that occur during message processing
            System.err.println("Error processing message: " + message);
            e.printStackTrace();
        }
    }

    /**
     * Called when the WebSocket connection is closed.
     *
     * @param code the closure code
     * @param reason the reason for closure
     * @param remote whether the closure was initiated by the remote peer
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from WebSocket server: " + reason);
    }

    /**
     * Called when an error occurs with the WebSocket connection.
     *
     * @param ex the exception that occurred
     */
    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }
}
