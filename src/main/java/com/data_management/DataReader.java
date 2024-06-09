package com.data_management;

import java.io.IOException;
import java.net.URI;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;

    /**
     * Connects to the specified URI to start reading data.
     *
     * @param serverUri the URI of the server to connect to
     * @throws IOException if there is an error connecting to the server
     */
    void connect(URI serverUri) throws IOException;

    /**
     * Handles incoming messages.
     *
     * @param message the message received
     */
    void onMessage(String message);
}
