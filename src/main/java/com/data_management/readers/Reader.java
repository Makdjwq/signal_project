package com.data_management.readers;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.readers.DataReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Reader implements DataReader {
    private String path;
    public Reader(String path){
        this.path = path;
    }

    /**
     * This method reads the data from a specified directory and adds the records to the dataStorage
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there was an error when reading the file
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException{
        // Path to the directory where data files are stored
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        Path pathToDirectory = Paths.get(path);

        try (Stream<Path> paths = Files.walk(pathToDirectory)) {
            paths.filter(Files::isRegularFile).forEach(file -> {
                try (Stream<String> lines = Files.lines(file)) {
                    lines.forEach(line -> {
                        try {
                            PatientRecordData recordData = parseLineToPatientRecord(line);
                            // Use the data to add patient information to storage
                            dataStorage.addPatientData(recordData.patientId, recordData.measurementValue, recordData.recordType, recordData.timestamp);
                        } catch (Exception e) {
                            System.err.println("Failed to parse or add data to storage: " + e.getMessage());
                        }
                    });
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file + " - " + e.getMessage());
                }
            });
        }
    }


    /**
     * This method is used to parse a line (String) to data we can add to the storage
     * @param line - parsed data
     * @return PatientRecordData - data later added to the storage
     */
    private PatientRecordData parseLineToPatientRecord(String line) {
        try {
            String[] parts = line.split(",");
            int patientId = Integer.parseInt(parts[0].split(": ")[1].trim());
            long timestamp = Long.parseLong(parts[1].split(": ")[1].trim());
            String recordType = parts[2].split(": ")[1].trim();

            String dataPart = parts[3].split(": ")[1].trim();
            double measurementValue = 0;  // Default or error value
            try {
                // Attempt to parse the measurement value if it's numeric
                measurementValue = Double.parseDouble(dataPart.replace("%", ""));
            } catch (NumberFormatException e) {//For triggered alerts
                //System.out.println("Non-numeric data found: '" + dataPart + "', using default measurement value: " + measurementValue);
                //System.out.println(line);
                //measurementValue = 999; // uses 999 to know that it's a trigger
            }

            PatientRecord record = new PatientRecord(patientId, measurementValue, recordType, timestamp);
            return new PatientRecordData(record, patientId, measurementValue, recordType, timestamp);
        } catch (Exception e) {
            System.err.println("Error parsing line: '" + line + "' - " + e.getMessage());
            return null;
        }
    }


    /**
     * Class used by previous methods to seperate specific things inside a line. Similar to PatientRecord class, however used in different purposes
     */
    public static class PatientRecordData {
        public PatientRecord patientRecord;
        public int patientId;
        public double measurementValue;
        public String recordType;
        public long timestamp;

        public PatientRecordData(PatientRecord patientRecord, int patientId, double measurementValue, String recordType, long timestamp) {
            this.patientRecord = patientRecord;
            this.patientId = patientId;
            this.measurementValue = measurementValue;
            this.recordType = recordType;
            this.timestamp = timestamp;
        }
    }

}


