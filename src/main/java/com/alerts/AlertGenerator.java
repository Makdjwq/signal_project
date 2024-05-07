package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    public List<Alert> alertList = new ArrayList<>();//Initialized so we can have access to it later

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE); // get all data

        // Loop used to iterate through all records of a patient
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);

            // Blood Pressure Data Alerts
            if (record.getRecordType().contains("Pressure")) {
                //System.out.println(record.getRecordType());
                if (shouldTriggerBloodPressureAlert(record, records)) {
                    // Expect the next record to be an alert
                    if (i + 1 < records.size()) {
                        System.out.println("Proper alert found for Blood Pressure condition."); // I do this to check if the alert was already manually initialized by a nurse
                    } else {
                        System.out.println("Expected alert missing or incorrect after Blood Pressure condition.");

                    }
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Combined Alert", record.getTimestamp()));
                }
            }
            //Blood Saturation Data Alerts
            else if ("Saturation".equals(record.getRecordType())) {

                if (shouldTriggerSaturationAlert(record, records)) {
                    // Expect the next record to be an alert
                    if (i + 1 < records.size() && records.get(i+1).getRecordType().equals("Alert")) {
                        System.out.println("Proper alert found for Saturation condition.");
                    } else {
                        System.out.println("Expected alert missing or incorrect after Saturation condition.");
                    }
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Blood Saturation Alert", record.getTimestamp()));
                }
            }
            //ECG Data Alerts
            else if("ECG".equals(record.getRecordType())){
                //HOW AM I SUPPOSED TO CONVERT THOSE VALUES TO HEART RATE?
                //System.out.println(record.getMeasurementValue());
                if (i > 0) {  // Ensure there's a previous record to compare intervals
                    PatientRecord previousRecord = records.get(i - 1);
                    if(checkIrregularBeatPattern(previousRecord, record) || checkAbnormalHeartRate(record)){
                        if (i + 1 < records.size() && records.get(i+1).getRecordType().equals("Alert")) {
                            System.out.println("Proper alert found for ECG condition.");
                        } else {
                            System.out.println("Expected alert missing or incorrect after ECG condition.");
                        }
                        triggerAlert(new Alert(String.valueOf(record.getPatientId()), "ECG Data Alert", record.getTimestamp()));
                    }
                }
            }
            //Combined alert
            else if(record.getRecordType().equals("SystolicPressure") || record.getRecordType().equals("Saturation")){
                if(record.getRecordType().equals("SystolicPressure") && record.getMeasurementValue() < 90){
                    long t = Long.MAX_VALUE;
                    int j = i-1;
                    while(t > 600000){
                        t = record.getTimestamp() - records.get(j).getTimestamp(); // Calculate the difference in time (has to be less than 10 min)
                        if(records.get(j).getRecordType().equals("Saturation")){
                            if(records.get(j).getMeasurementValue() < 92){
                                if (i + 1 < records.size() && records.get(i+1).getRecordType().equals("Alert")) {
                                    System.out.println("Proper alert found for Combination condition.");
                                } else {
                                    System.out.println("Expected alert missing or incorrect after Combination condition.");
                                }
                                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Combined Alert", record.getTimestamp()));
                            }
                        }
                        j--;
                    }
                }
                if(record.getRecordType().equals("Saturation") && record.getMeasurementValue() < 92){
                    long t = Long.MAX_VALUE;
                    int j = i-1;
                    while(t > 600000){
                        t = record.getTimestamp() - records.get(j).getTimestamp();
                        if(records.get(j).getRecordType().equals("SystolicPressure")){
                            if(records.get(j).getMeasurementValue() < 90){
                                if (i + 1 < records.size() && records.get(i+1).getRecordType().equals("Alert")) {
                                    System.out.println("Proper alert found for Combination condition.");
                                } else {
                                    System.out.println("Expected alert missing or incorrect after Combination condition.");
                                }
                                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Combined Alert", record.getTimestamp()));
                            }
                        }
                        j--;
                    }
                }
            }
            //Triggered Alerts
            else if(record.getRecordType().equals("Alert") ){
                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Triggered Alert", record.getTimestamp()));
            }
            else{// Was used to check what other types of data are there
                //System.out.println(record.getRecordType());
            }

        }
    }

    /**
     * Checks if the heart rate is within acceptable range
     * !!!!Do not know how to convert Hz to heart rate (the values are in the range of -1 and 1), whereas expected (40-110)!!!
     * @param record - singular patient record that may be dangerous for the patient
     * @return true if abnormal, false if normal
     */
    private boolean checkAbnormalHeartRate(PatientRecord record) {
        double heartRate = 60 *  record.getMeasurementValue();//convert somehow the function to heart rate

        if (heartRate < 50 || heartRate > 100) {
            //System.out.println(heartRate);
            //return true; ENABLE WHEN FIGURED OUT HOW TO CONVERT
            //triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Abnormal Heart Rate Detected: " + heartRate + " bpm", record.getTimestamp()));
        }
        return false;
    }

    /**
     * Checks if the beat pattern is irregular
     * @param previousRecord - previous record
     * @param currentRecord - current record
     * @return true if irregular, false if regular
     */

    private boolean checkIrregularBeatPattern(PatientRecord previousRecord, PatientRecord currentRecord) {
        long timeIntervalPrevious = previousRecord.getTimestamp();
        long timeIntervalCurrent = currentRecord.getTimestamp();
        long intervalDifference = Math.abs(timeIntervalCurrent - timeIntervalPrevious);

        // Assuming intervals should be fairly consistent; e.g., less than a 10% variation
        long expectedInterval = (long)(timeIntervalPrevious * 0.1); // 10% variation
        if (intervalDifference > expectedInterval) {
            return true;
            //triggerAlert(new Alert(String.valueOf(currentRecord.getPatientId()), "Irregular ECG Beat Pattern Detected", currentRecord.getTimestamp()));
        }
        return false;
    }

    /**
     * Checks if blood pressure alert should be triggered
     * @param record - current record
     * @param records - all records of the same patient
     * @return
     */
    private boolean shouldTriggerBloodPressureAlert(PatientRecord record, List<PatientRecord> records) {

        PatientRecord first = null;
        PatientRecord second = null;
        PatientRecord third = null;
        for (int i = 0; i < records.size(); i++) {
            PatientRecord current = records.get(i);
            if(current.equals(record)){
                if(i > 3) {
                    first = records.get(i - 3);
                    second = records.get(i - 2);
                    third = records.get(i - 1);
                    // Check for increasing trend
                    if ((second.getMeasurementValue() > first.getMeasurementValue() + 10) &&
                            (third.getMeasurementValue() > second.getMeasurementValue() + 10)) {
                        System.out.println("Increasing Trend Alert Triggered for Patient ID: " + current.getPatientId());
                        return true;
                    }

                    // Check for decreasing trend
                    if ((second.getMeasurementValue() < first.getMeasurementValue() - 10) &&
                            (third.getMeasurementValue() < second.getMeasurementValue() - 10)) {
                        System.out.println("Decreasing Trend Alert Triggered for Patient ID: " + current.getPatientId());
                        return true;
                    }
                }
                else break;
            }
        }

        if(record.getRecordType().equals("SystolicPressure")){
            return record.getMeasurementValue() < 90 || record.getMeasurementValue() > 180;
        }
        else{
            return record.getMeasurementValue() < 60 || record.getMeasurementValue() > 120;
        }
    }

    /**
     * Checks if saturation alert should be triggered
     * @param record - current record
     * @param records - all records of the same patient
     * @return true if yes, false if no
     */
    private boolean shouldTriggerSaturationAlert(PatientRecord record, List<PatientRecord> records) {
        double value = record.getMeasurementValue();
        long time = record.getTimestamp();
        for(PatientRecord patientRecord : records){
            if ("Saturation".equals(patientRecord.getRecordType())) {
                if (patientRecord.getTimestamp() > time - 600000 && patientRecord.getTimestamp() < time) {
                    if (patientRecord.getMeasurementValue()-5 >= value) {
                        System.out.println("The saturation was " + patientRecord.getMeasurementValue()+ " and now is " +value + " and the time difference is " + (patientRecord.getTimestamp() - time)/60000 + " minutes"); // Debugging purposes
                        System.out.println();
                        return true;
                    }
                }
            }
        }
        if(value < 92){
            System.out.println(value);
        }
        return value < 92;  // Low saturation level
    }


    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        //System.out.println(alert.getCondition());
        alertList.add(alert);
    }
}
