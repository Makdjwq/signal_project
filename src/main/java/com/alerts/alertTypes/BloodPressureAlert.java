package com.alerts.alertTypes;

import com.alerts.alertTypes.Alert;

public class BloodPressureAlert extends Alert {
    public BloodPressureAlert(String patientId, String condition, long timestamp) {
        super(patientId,  condition, timestamp);
    }

}
