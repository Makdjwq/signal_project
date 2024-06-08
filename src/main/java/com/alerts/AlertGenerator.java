package com.alerts;

import com.alerts.alertTypes.Alert;
import com.alerts.factories.AlertFactory;
import com.alerts.strategies.*;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * The AlertGenerator class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a  DataStorage instance to access patient data and evaluate
 * it against specific health criteria.
 *
 * !! Refactored after week 7, so it uses all patterns
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private AlertFactory alertFactory;
    public List<Alert> alertList = new ArrayList<>();//Initialized so we can have access to it later
    private List<AlertStrategy> alertStrategies = new ArrayList<>();

    /**
     * Constructs an AlertGenerator with a specified DataStorage.
     * The DataStorage is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alertFactory = new AlertFactory();
        initializeStrategies();
    }
    private void initializeStrategies() {
        alertStrategies.add(new BloodPressureStrategy());
        alertStrategies.add(new HeartRateStrategy());
        alertStrategies.add(new OxygenSaturationStrategy());
        alertStrategies.add(new CombinedAlertStrategy());
        alertStrategies.add(new TriggeredAlertStrategy());
    }
    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        for (AlertStrategy strategy : alertStrategies) {
            Alert alert = strategy.checkAlert(patient, alertFactory);
            if (alert != null) {
                triggerAlert(alert);
            }

        }
    }
    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        alertList.add(alert);
    }




}
