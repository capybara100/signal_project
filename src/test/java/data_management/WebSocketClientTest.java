package data_management;

import com.cardio_generator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.WebSocketClient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for WebSocketClient to validate its behavior and interactions.
 */
class WebSocketClientTest {

    private WebSocketClient webSocketClient;
    private DataStorage mockDataStorage;
    private WebSocketOutputStrategy server;

    /**
     * Set up the test environment by initializing mocks and starting the WebSocket server.
     *
     * @throws URISyntaxException if the URI syntax is incorrect
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    @BeforeEach
    void setUp() throws URISyntaxException, InterruptedException {
        mockDataStorage = mock(DataStorage.class);
        server = new WebSocketOutputStrategy(8888);
        Thread.sleep(1000); // Wait for server to start
        webSocketClient = new WebSocketClient(new URI("ws://localhost:8888"), mockDataStorage);
        webSocketClient.connect();
        Thread.sleep(1000); // Wait for client to connect
    }

    /**
     * Clean up the test environment by closing the WebSocket client and stopping the server.
     *
     * @throws Exception if an error occurs during cleanup
     */
    @AfterEach
    void tearDown() throws Exception {
        webSocketClient.close();
        if (server != null) {
            server.stopServer();
            server = null;
        }
        Thread.sleep(1000); // Ensure the server has time to release the port
    }

    /**
     * Test handling of invalid data format leading to NumberFormatException.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @DisplayName("Handle NumberFormatException")
    public void testOnMessageWithInvalidData() throws Exception {
        server.output(123, 1609459200L, "XYZ", "abc");
        Thread.sleep(1000);
        verify(mockDataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    /**
     * Test handling of invalid percentage in the message.
     */
    @Test
    @DisplayName("Handle invalid percentage in message")
    void testOnMessageWithInvalidPercentage() {
        String invalidMessage = "123,1609459200,XYZ,45";
        webSocketClient.onMessage(invalidMessage);
        verify(mockDataStorage).addPatientData(123, 45.0, "XYZ", 1609459200L);
    }

    /**
     * Test handling of messages with invalid data length.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    @Test
    @DisplayName("Data format test with invalid length")
    void testOnMessageWithInvalidDataLength() throws InterruptedException {
        server.output(123, 1609459200L, "XYZ", "abc");
        Thread.sleep(1000);
        verify(mockDataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    /**
     * Test handling of normal WebSocket closure.
     */
    @Test
    @DisplayName("Handle normal closure")
    void testOnClose() {
        webSocketClient.onClose(1000, "Normal closure", true);
        verify(mockDataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    /**
     * Test handling of errors in the WebSocket connection.
     */
    @Test
    @DisplayName("Handle error correctly")
    void testOnError() {
        webSocketClient.onError(new Exception("Test exception"));
        verify(mockDataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    /**
     * Test if the WebSocket server starts and stops correctly.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @DisplayName("Server starts and stops correctly")
    void testServerStartStop() throws Exception {
        server.stopServer();
        server = new WebSocketOutputStrategy(8888);
        Thread.sleep(1000); // Allow server to start again
        assertNotNull(server);
    }

    /**
     * Test handling of valid data and ensure it's parsed and stored correctly.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    @Test
    @DisplayName("Valid data is correctly parsed and stored")
    void testOnMessageWithValidData() throws InterruptedException {
        server.output(123, 1609459200L, "HeartRate", "75");
        Thread.sleep(1000);
        verify(mockDataStorage).addPatientData(123, 75.0, "HeartRate", 1609459200L);
    }

    /**
     * Test if multiple clients can connect to the WebSocket server simultaneously.
     *
     * @throws URISyntaxException if the URI syntax is incorrect
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    @Test
    @DisplayName("Multiple clients can connect simultaneously")
    void testMultipleClients() throws URISyntaxException, InterruptedException {
        WebSocketClient client2 = new WebSocketClient(new URI("ws://localhost:8888"), mockDataStorage);
        client2.connect();
        Thread.sleep(1000); // Allow second client to connect
        assertTrue(webSocketClient.isOpen());
        assertTrue(client2.isOpen());
        client2.close();
    }

    /**
     * Test handling of simultaneous messages sent to the WebSocket server.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    @Test
    @DisplayName("Simultaneous messages are handled correctly")
    void testSimultaneousMessages() throws InterruptedException {
        server.output(123, 1609459200L, "HeartRate", "75");
        server.output(124, 1609459300L, "BloodPressure", "80.5");
        Thread.sleep(1000);
        verify(mockDataStorage).addPatientData(123, 75.0, "HeartRate", 1609459200L);
        verify(mockDataStorage).addPatientData(124, 80.5, "BloodPressure", 1609459300L);
    }

    /**
     * Test if messages are broadcasted to multiple clients correctly.
     *
     * @throws URISyntaxException if the URI syntax is incorrect
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    @Test
    @DisplayName("Messages are broadcasted to multiple clients")
    void testBroadcastToMultipleClients() throws URISyntaxException, InterruptedException {
        WebSocketClient client2 = new WebSocketClient(new URI("ws://localhost:8888"), mockDataStorage);
        client2.connect();
        Thread.sleep(1000);
        server.output(123, 1609459200L, "HeartRate", "75");
        Thread.sleep(1000);
        // Each client should receive the message
        verify(mockDataStorage, times(2)).addPatientData(123, 75.0, "HeartRate", 1609459200L);
        client2.close();
    }
}
