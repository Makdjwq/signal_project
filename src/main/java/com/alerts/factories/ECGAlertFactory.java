package com.alerts.factories;

import com.alerts.alertTypes.ECGAlert;

public class ECGAlertFactory extends AlertFactory {
    @Override
    public ECGAlert createAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}
