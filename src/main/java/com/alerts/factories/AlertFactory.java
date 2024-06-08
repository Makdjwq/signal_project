package com.alerts.factories;


import com.alerts.alertTypes.Alert;

/**
 * Basic alert factory used to create standard alert
 */
public class AlertFactory {
    public Alert createAlert(String patientId, String condition, long timestamp){
        return new Alert(patientId, condition, timestamp);
    }
}

