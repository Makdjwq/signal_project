package com.alerts.strategies;

import com.alerts.alertTypes.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {
    @Override
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE); // get all data
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            if (record.getRecordType().contains("Pressure")) {
                //System.out.println(record.getRecordType());
                if (shouldTriggerBloodPressureAlert(record, records)) {
                    // Expect the next record to be an alert
                    if (i + 1 < records.size()) {
                        System.out.println("Proper alert found for Blood Pressure condition."); // I do this to check if the alert was already manually initialized by a nurse
                    } else {
                        System.out.println("Expected alert missing or incorrect after Blood Pressure condition.");

                    }
                    return true;
                }
            }
        }
        return false;
    }
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
}
