package com.alerts.strategies;

import com.alerts.alertTypes.Alert;
import com.alerts.factories.AlertFactory;
import com.data_management.Patient;

/**
 * Interface for alert strategies
 */
public interface AlertStrategy {
    Alert checkAlert(Patient patient, AlertFactory alertFactory);
}

