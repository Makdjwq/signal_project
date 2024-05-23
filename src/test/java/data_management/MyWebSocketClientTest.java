package data_management;

import com.cardio_generator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.MyWebSocketClient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is used to test if the websocket connections and receiving works
 */
public class MyWebSocketClientTest {

    private DataStorage dataStorage;
    private MyWebSocketClient client;
    private WebSocketOutputStrategy server;

    /**
     * Setting server side and client side before running tests
     * @throws URISyntaxException - URI exception
     * @throws InterruptedException - interrupted exception
     */
    @BeforeEach
    public void setup() throws URISyntaxException, InterruptedException {
        dataStorage = new DataStorage();
        client = new MyWebSocketClient(new URI("ws://localhost:8887"), dataStorage);

        server = new WebSocketOutputStrategy(8887);
        Thread.sleep(1000);  // Wait for the server to start
    }

    /**
     * First test used to check if a simple message sent is indeed added to the storage
     * @throws InterruptedException- interrupted exception
     */
    @Test
    public void testOnMessage() throws InterruptedException {
        client.connect();
        Thread.sleep(1000);  // Wait for the client to connect

        // Simulate sending a message from the server
        server.output(1, 1627947323000L, "HeartRate", "75");
        Thread.sleep(1000);  // Wait for the client to receive the message

        // Fetch the records from the data storage and see if the data is already there
        List<PatientRecord> records = dataStorage.getRecords(1, 1627947323000L, 1627947324000L);
        assertEquals(1, records.size());
        PatientRecord record = records.get(0);
        assertEquals(1, record.getPatientId());
        assertEquals(75, record.getMeasurementValue());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(1627947323000L, record.getTimestamp());

        // Close the connection from both sides
        client.close();
        server.stop();
    }

    /**
     * This test is used to check what happens to a message that has invalid data
     * @throws InterruptedException- interrupted exception
     */
    @Test
    public void testMalformedMessage() throws InterruptedException {
        client.connect();
        Thread.sleep(1000);  // Wait for the client to connect

        // Simulate sending a malformed message from the server
        server.output(1, 1627947323000L, "HeartRate", "invalid_data");
        Thread.sleep(1000);  // Wait for the client to process the message

        // Verify that no data was stored
        List<PatientRecord> records = dataStorage.getRecords(1, 1627947323000L, 1627947324000L);
        assertEquals(0, records.size());

        // Close the connection from both sides
        client.close();
        server.stop();
    }

    /**
     * Used to check two different outputs
     * @throws InterruptedException- interrupted exception
     */
    @Test
    public void testRealTimeDataProcessing() throws InterruptedException {
        client.connect();
        Thread.sleep(1000);  // Wait for the client to connect

        // Simulate sending multiple messages from the server
        server.output(1, 1627947323000L, "HeartRate", "75");
        server.output(1, 1627947324000L, "Temperature", "36.6");
        Thread.sleep(1000);  // Wait for the client to receive the messages

        // Fetch the records from the data storage and verify both entries
        List<PatientRecord> records = dataStorage.getRecords(1, 1627947323000L, 1627947325000L);
        assertEquals(2, records.size());

        PatientRecord record1 = records.get(0);
        assertEquals(1, record1.getPatientId());
        assertEquals(75, record1.getMeasurementValue());
        assertEquals("HeartRate", record1.getRecordType());
        assertEquals(1627947323000L, record1.getTimestamp());

        PatientRecord record2 = records.get(1);
        assertEquals(1, record2.getPatientId());
        assertEquals(36.6, record2.getMeasurementValue());
        assertEquals("Temperature", record2.getRecordType());
        assertEquals(1627947324000L, record2.getTimestamp());

        // Close the connection from both sides
        client.close();
        server.stop();
    }

}
