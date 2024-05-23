package data_management;
import static org.junit.jupiter.api.Assertions.*;

import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.BloodOxygenAlert;
import com.alerts.alertTypes.BloodPressureAlert;
import com.alerts.alertTypes.ECGAlert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.ECGAlertFactory;
import org.junit.jupiter.api.Test;

public class AlertFactoryTest {
    @Test
    public void testBloodPressureAlertFactory() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("123","High Blood Pressure",16278482830000L);
        assertTrue(alert instanceof BloodPressureAlert);
        assertEquals("123", alert.getPatientId());
        assertEquals("High Blood Pressure", alert.getCondition());
    }

    @Test
    public void testBloodOxygenAlertFactory() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("456","Low Oxygen",1627848283000L);
        assertTrue(alert instanceof BloodOxygenAlert);
        assertEquals("456", alert.getPatientId());
        assertEquals("Low Oxygen", alert.getCondition());
    }

    @Test
    public void testECGAlertFactory() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("789","Irregular Heartbeat",1627848283000L);
        assertTrue(alert instanceof ECGAlert);
        assertEquals("789", alert.getPatientId());
        assertEquals("Irregular Heartbeat", alert.getCondition());
    }
}
