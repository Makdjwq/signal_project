package com.alerts.strategies;

import com.alerts.alertTypes.Alert;
import com.alerts.factories.AlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Class used to check triggered alerts
 */
public class TriggeredAlertStrategy implements AlertStrategy {
    @Override
    public Alert checkAlert(Patient patient, AlertFactory alertFactory) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE); // get all data
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            if (record.getRecordType().equals("Alert")) {
                return alertFactory.createAlert(
                        String.valueOf(record.getPatientId()),
                        "Triggered Alert",
                        record.getTimestamp()
                );
            }
        }
        return null;
    }
}
