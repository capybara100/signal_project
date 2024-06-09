package com.data_management;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * This class implements the DataReader interface to read data from files in a specified directory.
 * It parses the data and stores it into a DataStorage instance for further processing.
 */
public class DataReaderImplementation implements DataReader {
    private final Path directoryPath;

    /**
     * Constructor to initialize the directory path where data files are located.
     *
     * @param directoryPath the path of the directory containing data files
     */
    public DataReaderImplementation(Path directoryPath) {
        this.directoryPath = directoryPath;
    }

    /**
     * Reads data from all files in the specified directory and stores it into the provided DataStorage instance.
     *
     * @param dataStorage the DataStorage instance where parsed data will be stored
     * @throws IOException if an I/O error occurs reading from the directory or its files
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        File directory = directoryPath.toFile();

        // Check if the specified directory exists and is a directory
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Specified directory does not exist or is not a directory.");
        }

        // Iterate through files in the directory
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                // Only process regular files
                if (file.isFile()) {
                    parseFile(file, dataStorage);
                }
            }
        }
    }
    /**
     * Connects to a WebSocket server at the specified URI.
     *
     * @param serverUri the URI of the WebSocket server
     * @throws IOException if an I/O error occurs while connecting to the server
     */
    @Override
    public void connect(URI serverUri) throws IOException {
        try {
            WebSocketClient client = new WebSocketClient(serverUri, DataStorage.getInstance());
            client.connect();
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI", e);
        }

    }

    /**
     * Handles incoming messages from the WebSocket server.
     *
     * @param message the message received from the server
     */
    @Override
    public void onMessage(String message) {

    }

    /**
     * Parses a single file and stores its data into the provided DataStorage instance.
     *
     * @param file the file to be parsed
     * @param dataStorage the DataStorage instance where parsed data will be stored
     * @throws IOException if an I/O error occurs reading from the file
     */
    private void parseFile(File file, DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming data is formatted as: patientId, measurementValue, measurementType, timestamp
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    try {
                        int patientId = Integer.parseInt(parts[0]);
                        double measurementValue = Double.parseDouble(parts[1]);
                        String measurementType = parts[2];
                        long timestamp = Long.parseLong(parts[3]);
                        // Add parsed data to DataStorage
                        dataStorage.addPatientData(patientId, measurementValue, measurementType, timestamp);
                    } catch (NumberFormatException e) {
                        // Handle parsing errors by logging or printing an error message
                        System.err.println("Error parsing data from file: " + e.getMessage());
                    }
                } else {
                    // Handle invalid data format by logging or printing an error message
                    System.err.println("Invalid data format in file: " + file.getName());
                }
            }
        }
    }
}
