package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements the {@link OutputStrategy} interface for writing health data to files.
 * This class manages file operations such as creating directories and writing data to specific files
 * designated by data labels. Each type of data (label) is stored in its own file.
 */
public class FileOutputStrategy implements OutputStrategy {

    private String baseDirectory;
    // Changed variable name to be UPPER_SNAKE_CASE.
    /**
     * Stores the file paths associated with each data label to minimize repeated path computations.
     */
    public final ConcurrentHashMap<String, String> FILE_MAP = new ConcurrentHashMap<>();
    /**
     * Constructs a new FileOutputStrategy with a specified base directory where all files will be stored.
     *
     * @param baseDirectory the directory path where output files will be created and managed.
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs the specified health data to a file. This method ensures that each data type (label) is written
     * to a dedicated file. It handles file creation and writing, ensuring data is appended to existing content.
     *
     * @param patientId The unique identifier of the patient for whom the data is generated.
     * @param timestamp The time at which the data was generated, represented as a long timestamp.
     * @param label A descriptive label for the type of data being output, such as "Heart Rate" or "Blood Pressure".
     *              This label helps in categorizing and processing the output data correctly.
     * @param data The actual health data generated for the patient, formatted as a string.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the filePath variable
        // Changed variable name to be camelCase.
        String filePath = FILE_MAP.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}