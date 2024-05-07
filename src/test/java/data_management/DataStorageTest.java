package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.Patient;
import com.data_management.Reader;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.io.IOException;
import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() throws IOException {

        // DataReader reader
        DataStorage storage = new DataStorage();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
        assertEquals(200.0, records.get(1).getMeasurementValue()); // Validate first record

        //Test the reader in mockData folder
        Reader reader = new Reader("mockData"); // This data was created by using HealthDataSimulator with FileOutputStrategy
        DataStorage newStorage = new DataStorage();
        reader.readData(newStorage);
        newStorage.addPatientData(12345, 98, "Saturation", 1714376789000L);// Adding another patient for test purposes
        newStorage.addPatientData(12345, 92, "Saturation", 1714376789100L);
        //These are for manual checking if everything works fine (it does)
        /*newStorage.addPatientData(12345, -1, "ECG", 1714376789000L);// Adding another patient for test purposes
        newStorage.addPatientData(12345, 1, "ECG", 1714376789100L);
        newStorage.addPatientData(12345, 98, "Saturation", 1714376789000L);// Adding another patient for test purposes
        newStorage.addPatientData(12345, 92, "Saturation", 1714376789100L);*/

        List<PatientRecord> list = newStorage.getRecords(49, 0, Long.MAX_VALUE);
        assertEquals("Alert", list.get(0).getRecordType());//Checks if correctly reads and analizes the mock data
        assertEquals(0, list.get(1).getMeasurementValue());

        List<PatientRecord> list2 = newStorage.getRecords(12345, 0, Long.MAX_VALUE);
        assertEquals("Saturation", list2.get(0).getRecordType());//Checks if correctly reads and analizes the added patient
        assertEquals(92, list2.get(1).getMeasurementValue());


        // Initialize the AlertGenerator with the new storage
        AlertGenerator alertGenerator = new AlertGenerator(newStorage);

        // Evaluate all patients' data
        for (Patient patient : newStorage.getAllPatients()) {
            alertGenerator.evaluateData(patient);

        }
        assertEquals("1", alertGenerator.alertList.get(0).getPatientId()); // Checks if correctly initializes alerts for the mock data
        assertEquals("Triggered Alert", alertGenerator.alertList.get(0).getCondition());
        for (Alert alert : alertGenerator.alertList) {
            if (alert.getPatientId().equals("12345")) { // testing test patient
                assertEquals("Blood Saturation Alert", alert.getCondition());//Assert if correctly identifies the test patient condition
            }
        }
        newStorage.addPatientData(123456, -5, "ECG", 1714376789000L);// Adding another patient for test purposes
        newStorage.addPatientData(123456, 5, "ECG", 1914376789000L);

        // Initialize the AlertGenerator with the new storage
        AlertGenerator secondAlertGenerator = new AlertGenerator(newStorage);

        // Evaluate all patients' data
        for (Patient patient : newStorage.getAllPatients()) {
            secondAlertGenerator.evaluateData(patient);
        }
        for (Alert alert : secondAlertGenerator.alertList) {

            if (alert.getPatientId().equals("123456")) { // testing test patient
                assertEquals("ECG Data Alert", alert.getCondition());//Assert if correctly identifies the test patient condition
            }
        }

        newStorage.addPatientData(1234567, 70, "SystolicPressure", 1714376789000L);// Adding another patient for test purposes
        newStorage.addPatientData(1234567, 70, "Saturation", 1914376789000L);

        // Initialize the AlertGenerator with the new storage
        AlertGenerator thirdAlertGenerator = new AlertGenerator(newStorage);

        // Evaluate all patients' data
        for (Patient patient : newStorage.getAllPatients()) {
            thirdAlertGenerator.evaluateData(patient);
        }
        for (Alert alert : thirdAlertGenerator.alertList) {

            if (alert.getPatientId().equals("1234567")) { // testing test patient
                assertTrue(alert.getCondition().equals("Combined Alert") ||
                                        alert.getCondition().equals("Blood Saturation Alert"), alert.getCondition());//Assert if correctly identifies the test patient condition
            }
        }
    }
}
