package com.alerts.alertTypes;

import com.alerts.alertTypes.Alert;

public class ECGAlert extends Alert {
    public ECGAlert(String patientId, String condition, long timestamp){
        super( patientId,  condition, timestamp);
    }
}
