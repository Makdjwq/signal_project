package com.alerts.strategies;

import com.alerts.alertTypes.Alert;
import com.alerts.factories.AlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;
/**
 * Method used to check if a combined alert should be triggered
 */
public class CombinedAlertStrategy implements AlertStrategy {
    @Override
    public Alert checkAlert(Patient patient, AlertFactory alertFactory) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE); // get all data
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            if (record.getRecordType().equals("SystolicPressure") || record.getRecordType().equals("Saturation")) {
                if (record.getRecordType().equals("SystolicPressure") && record.getMeasurementValue() < 90) {
                    long t = Long.MIN_VALUE;
                    if(i > 0) {
                        int j = i - 1;
                        while (t < 600000) {
                            t = record.getTimestamp() - records.get(j).getTimestamp(); // Calculate the difference in time (has to be less than 10 min)
                            if (records.get(j).getRecordType().equals("Saturation")) {
                                if (records.get(j).getMeasurementValue() < 92) {
                                    return alertFactory.createAlert(
                                            String.valueOf(record.getPatientId()),
                                            "Combined Alert",
                                            record.getTimestamp()
                                    );
                                }
                            }
                            j--;
                        }
                    }
                }
                if (record.getRecordType().equals("Saturation") && record.getMeasurementValue() < 92) {
                    long t = Long.MIN_VALUE;
                    if(i > 0) {
                        int j = i - 1;
                        while (t < 600000) {
                            t = record.getTimestamp() - records.get(j).getTimestamp();
                            if (records.get(j).getRecordType().equals("SystolicPressure")) {
                                if (records.get(j).getMeasurementValue() < 90) {
                                    return alertFactory.createAlert(
                                            String.valueOf(record.getPatientId()),
                                            "Combined Alert",
                                            record.getTimestamp()
                                    );
                                }
                            }
                            j--;
                        }
                    }
                }
            }

        }
        return null;
    }
}
