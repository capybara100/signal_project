package alerts;

import com.alerts.*;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.ECGAlertFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the alert factories.
 */
public class AlertFactoryTest {
    /**
     * Tests the BloodPressureAlertFactory to ensure it creates the correct alert type.
     */
    @Test
    public void testBloodPressureAlertFactory(){
        // Test the BloodPressureAlertFactory to ensure it creates the correct alert type
        AlertFactory alertFactory = new BloodPressureAlertFactory();
        Alert alert = alertFactory.createAlert("1", "High Blood Pressure", System.currentTimeMillis());

        assertTrue(alert instanceof BloodPressureAlert); // Ensure the alert is of the correct type
        assertEquals("1", alert.getPatientId()); // Validate the patient ID
        assertEquals("High Blood Pressure", alert.getCondition()); // Validate the alert condition
    }

    /**
     * Tests the BloodOxygenAlertFactory to ensure it creates the correct alert type.
     */
    @Test
    public void testBloodOxygenAlertFactory(){
        // Test the BloodOxygenAlertFactory to ensure it creates the correct alert type
        AlertFactory alertFactory = new BloodOxygenAlertFactory();
        Alert alert = alertFactory.createAlert("1", "Low Blood Oxygen", System.currentTimeMillis());

        assertTrue(alert instanceof BloodOxygenAlert); // Ensure the alert is of the correct type
        assertEquals("1", alert.getPatientId()); // Validate the patient ID
        assertEquals("Low Blood Oxygen", alert.getCondition()); // Validate the alert condition
    }

    /**
     * Tests the ECGAlertFactory to ensure it creates the correct alert type.
     */
    @Test
    public void testECGAlertFactory(){
        // Test the ECGAlertFactory to ensure it creates the correct alert type
        AlertFactory alertFactory = new ECGAlertFactory();
        Alert alert = alertFactory.createAlert("1", "ECG is Abnormal", System.currentTimeMillis());

        assertTrue(alert instanceof ECGAlert); // Ensure the alert is of the correct type
        assertEquals("1", alert.getPatientId()); // Validate the patient ID
        assertEquals("ECG is Abnormal", alert.getCondition()); // Validate the alert condition
    }
}