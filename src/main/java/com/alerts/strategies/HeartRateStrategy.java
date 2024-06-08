package com.alerts.strategies;

import com.alerts.alertTypes.Alert;
import com.alerts.factories.AlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Strategy for heart rate, used to trigger alerts
 */
public class HeartRateStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient, AlertFactory alertFactory) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE); // get all data
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            if("ECG".equals(record.getRecordType())){
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
                        return alertFactory.createAlert(
                                String.valueOf(record.getPatientId()),
                                "Heart Rate Alert",
                                record.getTimestamp()
                        );
                    }
                }
            }
        }
        return null;
    }

    /**
     * Method used to check irregular beat patterns
     * @param previousRecord previous record
     * @param currentRecord current record
     * @return true if should trigger alert, false if no
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
     * Method used to check if there is an abnormal heart rate
     * @param record - current record
     * @return true if should trigger alert, false if no
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
}
