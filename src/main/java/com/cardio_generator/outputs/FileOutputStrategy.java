package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;
// Class name changed to fit UpperCamelCase convention
public class FileOutputStrategy implements OutputStrategy {
    // Changed field name to fit lowerCamelCase as it should not have underscore and should be in lowerCamelCase
    private String baseDirectory;
    // Changed file_map to fit naming conevention 
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();
    // Changed the constructor to have the same name as the class
    public FileOutputStrategy(String baseDirectory) {
        // Changed field name to fit lowerCamelCase
        this.baseDirectory = baseDirectory;
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory)); // Changed field name to fit lowerCamelCase
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String FilePath = file_map.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString()); // Changed field name to fit lowerCamelCase

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}
 
