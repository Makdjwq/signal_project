package data_management;
import com.alerts.alertTypes.Alert;
import com.alerts.factories.AlertFactory;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.alerts.strategies.OxygenSaturationStrategy;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertStrategyTest {
    @Test
    public void testNoBloodPressureAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(120, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(80, "DiastolicPressure", System.currentTimeMillis());

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert(patient, new AlertFactory());
        assertNull(alert);

    }

    @Test
    public void testHighSystolicPressureAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(185, "SystolicPressure", System.currentTimeMillis());

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert(patient, new AlertFactory());
        assertNotNull(alert);
        assertEquals("1", alert.getPatientId());
        assertEquals("Blood Pressure Alert", alert.getCondition());
    }

    @Test
    public void testLowDiastolicPressureAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(55, "DiastolicPressure", System.currentTimeMillis());

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert(patient, new AlertFactory());
        assertNotNull(alert);
        assertEquals("1", alert.getPatientId());
        assertEquals("Blood Pressure Alert", alert.getCondition());
    }

    @Test
    public void testIncreasingBloodPressureTrendAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(100, "SystolicPressure", System.currentTimeMillis() - 30000);
        patient.addRecord(115, "SystolicPressure", System.currentTimeMillis() - 20000);
        patient.addRecord(130, "SystolicPressure", System.currentTimeMillis() - 10000);

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert(patient, new AlertFactory());
        assertNull(alert);
    }

    @Test
    public void testNoOxygenSaturationAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(95, "Saturation", System.currentTimeMillis());

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        Alert alert = strategy.checkAlert(patient, new AlertFactory());
        assertNull(alert);

    }

    @Test
    public void testLowOxygenSaturationAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(90, "Saturation", System.currentTimeMillis());

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        Alert alert = strategy.checkAlert(patient, new AlertFactory());
        assertNotNull(alert);
        assertEquals("1", alert.getPatientId());
        assertEquals("Oxygen Saturation Alert", alert.getCondition());
    }

    @Test
    public void testDecreasingOxygenSaturationTrendAlert() {
        long currentTime = System.currentTimeMillis();
        Patient patient = new Patient(1);
        patient.addRecord(97, "Saturation", currentTime - 600000);
        patient.addRecord(92, "Saturation", currentTime - 300000);
        patient.addRecord(85, "Saturation", currentTime);

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        Alert alert = strategy.checkAlert(patient, new AlertFactory());
        assertNotNull(alert);
        assertEquals("1", alert.getPatientId());
        assertEquals("Oxygen Saturation Alert", alert.getCondition());
    }

    @Test
    public void testNoHeartRateAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(1.0, "ECG", System.currentTimeMillis());

        HeartRateStrategy strategy = new HeartRateStrategy();
        Alert alert = strategy.checkAlert(patient, new AlertFactory());
        assertNull(alert);

    }

    @Test
    public void testIrregularBeatPatternAlert() {
        long currentTime = System.currentTimeMillis();
        Patient patient = new Patient(1);
        patient.addRecord(1.0, "ECG", currentTime - 2000);
        patient.addRecord(1.0, "ECG", currentTime - 1000);
        patient.addRecord(1.0, "ECG", currentTime);

        HeartRateStrategy strategy = new HeartRateStrategy();
        Alert alert = strategy.checkAlert(patient, new AlertFactory());
        assertNull(alert);

    }

    @Test
    public void testAbnormalHeartRateAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(1.0, "ECG", System.currentTimeMillis());

        HeartRateStrategy strategy = new HeartRateStrategy();
        Alert alert = strategy.checkAlert(patient, new AlertFactory());
        assertNull(alert);

    }
}
