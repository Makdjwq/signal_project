package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates alert for patients within the simulation.
 * This class manages alerts which can either be in a "pressed" (active) state or "resolved" (inactive) state,
 * simulating conditions that would trigger or resolve alerts for a patient.
 */
public class AlertGenerator implements PatientDataGenerator {
    public static final Random RANDOM_GENERATOR = new Random(); // Changed the name to be UPPER_SNAKE_CASE.
    // Changed the AlertStates to be camelCase and 'False' to be upper case.
    private boolean[] alertStates; // False = resolved, true = pressed

    /**
     * Constructs an AlertGenerator for a specified number of patients.
     * Initializes all patients' alert states to "resolved" (false) by default (Java feature).
     *
     * @param patientCount The number of patients for whom alert data will be generated.
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates and outputs alert status for a specified patient.
     * Alerts can randomly resolve themselves or be triggered based on predefined conditions.
     *
     * @param patientId The unique identifier for the patient for whom data is to be generated.
     *                  This ID is used to associate generated data with specific patients.
     * @param outputStrategy The strategy for outputting the generated data, such as to a console,
     *                       file, or over network protocols like WebSocket or TCP.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Changed 'lambda' to be camelCase
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency.
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period.
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
