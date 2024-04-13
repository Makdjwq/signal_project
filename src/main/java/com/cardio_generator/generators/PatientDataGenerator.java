package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;
/**
 * Defines the contract for patient data generators within the health data simulation system.
 * Implementations of this interface are responsible for generating specific types of health data
 * for each patient and dispatching that data through output strategy.
 */
public interface PatientDataGenerator {
    /**
     * Generates health data for a specified patient and outputs it using the given output strategy.
     * Implementations of this method should focus on simulating data based on predefined
     * parameters or randomized values.
     *
     * @param patientId The unique identifier for the patient for whom data is to be generated.
     *                  This ID is used to associate generated data with specific patients.
     * @param outputStrategy The strategy for outputting the generated data, such as to a console,
     *                       file, or over network protocols like WebSocket or TCP.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
