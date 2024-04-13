package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates blood saturation data for patients as part of a health monitoring simulation.
 * This class simulates fluctuations in blood saturation levels and enables the output
 * of the generated data.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;

    /**
     * Constructs a BloodSaturationDataGenerator for a specified number of patients.
     * Initializes baseline saturation values for each patient within a normal range.
     *
     * @param patientCount The number of patients for whom saturation data will be generated.
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates and outputs a new saturation reading for a specified patient.
     * The method simulates fluctuations in blood saturation and ensures
     * the values remain within a healthy range (90% - 100%).
     *
     * @param patientId The unique identifier for the patient for whom data is to be generated.
     *                  This ID is used to associate generated data with specific patients.
     * @param outputStrategy The strategy for outputting the generated data, such as to a console,
     *                       file, or over network protocols like WebSocket or TCP.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
