package com.alerts.strategies;

import com.data_management.Patient;

// Strategy interface
public interface AlertStrategy {
    boolean checkAlert(Patient patient);
}

