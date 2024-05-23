package com.alerts.strategies;

import com.alerts.alertTypes.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {

    @Override
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE); // get all data
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            if ("Saturation".equals(record.getRecordType())) {

                if (shouldTriggerSaturationAlert(record, records)) {
                    // Expect the next record to be an alert
                    if (i + 1 < records.size() && records.get(i+1).getRecordType().equals("Alert")) {
                        System.out.println("Proper alert found for Saturation condition.");
                    } else {
                        System.out.println("Expected alert missing or incorrect after Saturation condition.");
                    }
                    return true;
                }
            }
        }
        return false;
    }
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
}
