package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.alertTypes.Alert;
import com.alerts.AlertGenerator;
import com.alerts.factories.AlertFactory;
import com.data_management.Patient;
import com.data_management.readers.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.io.IOException;
import java.util.List;

class DataStorageTest {

    private DataStorage dataStorage;
    private DataStorage newStorage;
    private AlertFactory alertFactory;

    @BeforeEach
    void setUp() {
        dataStorage = new DataStorage();
        newStorage = new DataStorage();
        alertFactory = new AlertFactory();
    }

    @Test
    void testAddAndGetRecords() throws IOException {
        dataStorage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        dataStorage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
        assertEquals(200.0, records.get(1).getMeasurementValue()); // Validate second record
    }

    @Test
    void testReadDataAndAddPatientData() throws IOException {
        Reader reader = new Reader("mockData");
        reader.readData(newStorage);
        newStorage.addPatientData(12345, 98, "Saturation", 1714376789000L);
        newStorage.addPatientData(12345, 92, "Saturation", 1714376789100L);

        List<PatientRecord> list = newStorage.getRecords(49, 0, Long.MAX_VALUE);
        assertEquals("Alert", list.get(0).getRecordType());
        assertEquals(0, list.get(1).getMeasurementValue());

        List<PatientRecord> list2 = newStorage.getRecords(12345, 0, Long.MAX_VALUE);
        assertEquals("Saturation", list2.get(0).getRecordType());
        assertEquals(92, list2.get(1).getMeasurementValue());
    }

    @Test
    void testEvaluateDataWithMockData() throws IOException {
        Reader reader = new Reader("mockData");
        reader.readData(newStorage);
        newStorage.addPatientData(12345, 98, "Saturation", 1714376789000L);
        newStorage.addPatientData(12345, 92, "Saturation", 1714376789100L);

        AlertGenerator alertGenerator = new AlertGenerator(newStorage);
        for (Patient patient : newStorage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }

        assertFalse(alertGenerator.alertList.isEmpty());
        assertEquals("Oxygen Saturation Alert", alertGenerator.alertList.get(0).getCondition());

        for (Alert alert : alertGenerator.alertList) {
            if (alert.getPatientId().equals("12345")) {
                assertEquals("Oxygen Saturation Alert", alert.getCondition());
            }
        }
    }

    @Test
    void testEvaluateDataWithNewPatientECG() throws IOException {
        newStorage.addPatientData(123456, -5, "ECG", 1714376789000L);
        newStorage.addPatientData(123456, 5, "ECG", 1914376789000L);

        AlertGenerator alertGenerator = new AlertGenerator(newStorage);
        for (Patient patient : newStorage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }

        for (Alert alert : alertGenerator.alertList) {
            if (alert.getPatientId().equals("123456")) {
                assertEquals("Heart Rate Alert", alert.getCondition());
            }
        }
    }

    @Test
    void testEvaluateDataWithNewPatientCombinedAlert() throws IOException {
        newStorage.addPatientData(1234567, 70, "SystolicPressure", 1714376789000L);
        newStorage.addPatientData(1234567, 70, "Saturation", 1714376789000L);

        AlertGenerator alertGenerator = new AlertGenerator(newStorage);
        for (Patient patient : newStorage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }

        for (Alert alert : alertGenerator.alertList) {
            if (alert.getPatientId().equals("1234567")) {
                //System.out.println(alert.getCondition());
                assertTrue((alert.getCondition().equals("Combined Alert") ||
                        alert.getCondition().equals("Blood Pressure Alert")) || alert.getCondition().equals("Oxygen Saturation Alert"), alert.getCondition());
            }
        }
    }
}
