package com.alerts.alertTypes;

import com.alerts.alertTypes.Alert;

public class BloodOxygenAlert extends Alert {
    public BloodOxygenAlert(String patientId, String condition, long timestamp) {
        super(patientId,  condition, timestamp);
    }
}