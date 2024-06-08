package com.alerts.strategies;

import com.alerts.alertTypes.Alert;
import com.alerts.factories.AlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Strategy for oxygen saturation, used to trigger alerts
 */
public class OxygenSaturationStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient, AlertFactory alertFactory) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE); // get all data
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            if ("Saturation".equals(record.getRecordType())) {

                if (shouldTriggerSaturationAlert(record, records)) {
                    return alertFactory.createAlert(
                            String.valueOf(record.getPatientId()),
                            "Oxygen Saturation Alert",
                            record.getTimestamp()
                    );
                }
            }
        }
        return null;
    }

    /**
     * Method used to check saturation
     * @param record - current record
     * @param records - all records
     * @return true if should trigger alert, false if no
     */
    private boolean shouldTriggerSaturationAlert(PatientRecord record, List<PatientRecord> records) {
        double value = record.getMeasurementValue();
        long time = record.getTimestamp();
        for(PatientRecord patientRecord : records){
            if ("Saturation".equals(patientRecord.getRecordType())) {
                if (patientRecord.getTimestamp() > time - 600000 && patientRecord.getTimestamp() < time) {
                    if (patientRecord.getMeasurementValue()-5 >= value) {
//                        System.out.println("The saturation was " + patientRecord.getMeasurementValue()+ " and now is " +value + " and the time difference is " + (patientRecord.getTimestamp() - time)/60000 + " minutes"); // Debugging purposes
//                        System.out.println();
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
