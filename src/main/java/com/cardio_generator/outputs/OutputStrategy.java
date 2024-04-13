package com.cardio_generator.outputs;
/**
 * Defines the contract for output strategies within the health data simulation system.
 * Implementations of this interface are responsible for handling the output of generated health data,
 * ensuring that the data is dispatched appropriately according to the configured output method.
 */
public interface OutputStrategy {
    /**
     * Outputs the specified health data for a patient at a given timestamp.
     * This method is responsible for formatting and dispatching the data based on the implementation
     * details of the output strategy, whether it's writing to the console, saving to a file, or sending over a network.
     *
     * @param patientId The unique identifier of the patient for whom the data is generated.
     * @param timestamp The time at which the data was generated, represented as a long timestamp.
     * @param label A descriptive label for the type of data being output, such as "Heart Rate" or "Blood Pressure".
     *              This label helps in categorizing and processing the output data correctly.
     * @param data The actual health data generated for the patient, formatted as a string.
     */
    void output(int patientId, long timestamp, String label, String data);
}
