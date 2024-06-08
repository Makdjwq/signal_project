package com.alerts.alertTypes;

import com.alerts.alertTypes.Alert;
/**
 * Specific alert for blood  pressure
 */
public class BloodPressureAlert extends Alert {
    public BloodPressureAlert(String patientId, String condition, long timestamp) {
        super(patientId,  condition, timestamp);
    }

}
