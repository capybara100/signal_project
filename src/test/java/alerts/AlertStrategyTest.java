package alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.alerts.strategies.OxygenSaturationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the alert strategies.
 */
public class AlertStrategyTest {

    private Patient patient;

    @BeforeEach
    public void setUp() {
        patient = mock(Patient.class);
    }

    /**
     * Tests the BloodPressureStrategy for detecting high blood pressure.
     */
    @Test
    public void testBloodPressureStrategyHighBloodPressure() {
        BloodPressureStrategy strategy = new BloodPressureStrategy();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 185, "SystolicPressure", 1700000000000L),
                new PatientRecord(1, 125, "DiastolicPressure", 1700000000001L)
        );
        when(patient.getRecords(anyLong(), anyLong())).thenReturn(records);

        assertTrue(strategy.checkAlert(patient));
    }

    /**
     * Tests the BloodPressureStrategy for detecting normal blood pressure.
     */
    @Test
    public void testBloodPressureStrategyNormalBloodPressure() {
        BloodPressureStrategy strategy = new BloodPressureStrategy();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 120, "SystolicPressure", 1700000000000L),
                new PatientRecord(1, 80, "DiastolicPressure", 1700000000001L)
        );
        when(patient.getRecords(anyLong(), anyLong())).thenReturn(records);

        assertFalse(strategy.checkAlert(patient));
    }

    /**
     * Tests the BloodPressureStrategy for detecting blood pressure variation.
     */
    @Test
    public void testBloodPressureStrategyBloodPressureVariation() {
        BloodPressureStrategy strategy = new BloodPressureStrategy();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 100, "SystolicPressure", 1700000000000L),
                new PatientRecord(1, 115, "SystolicPressure", 1700000001000L),
                new PatientRecord(1, 130, "SystolicPressure", 1700000002000L)
        );
        when(patient.getRecords(anyLong(), anyLong())).thenReturn(records);

        assertTrue(strategy.checkAlert(patient));
    }

    /**
     * Tests the HeartRateStrategy for detecting high heart rate.
     */
    @Test
    public void testHeartRateStrategyHighHeartRate() {
        HeartRateStrategy strategy = new HeartRateStrategy();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 130, "HeartRate", 1700000000000L)
        );
        when(patient.getRecords(anyLong(), anyLong())).thenReturn(records);

        assertTrue(strategy.checkAlert(patient));
    }

    /**
     * Tests the HeartRateStrategy for detecting normal heart rate.
     */
    @Test
    public void testHeartRateStrategyNormalHeartRate() {
        HeartRateStrategy strategy = new HeartRateStrategy();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 70, "HeartRate", 1700000000000L)
        );
        when(patient.getRecords(anyLong(), anyLong())).thenReturn(records);

        assertFalse(strategy.checkAlert(patient));
    }

    /**
     * Tests the HeartRateStrategy for detecting heart rate variation.
     */
    @Test
    public void testHeartRateStrategyHeartRateVariation() {
        HeartRateStrategy strategy = new HeartRateStrategy();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 60, "HeartRate", 1700000000000L),
                new PatientRecord(1, 80, "HeartRate", 1700000001000L),
                new PatientRecord(1, 100, "HeartRate", 1700000002000L)
        );
        when(patient.getRecords(anyLong(), anyLong())).thenReturn(records);

        assertTrue(strategy.checkAlert(patient));
    }

    /**
     * Tests the OxygenSaturationStrategy for detecting low oxygen saturation.
     */
    @Test
    public void testOxygenSaturationStrategyLowOxygenSaturation() {
        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 85, "Saturation", 1700000000000L)
        );
        when(patient.getRecords(anyLong(), anyLong())).thenReturn(records);

        assertTrue(strategy.checkAlert(patient));
    }

    /**
     * Tests the OxygenSaturationStrategy for detecting normal oxygen saturation.
     */
    @Test
    public void testOxygenSaturationStrategyNormalOxygenSaturation() {
        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 95, "Saturation", 1700000000000L)
        );
        when(patient.getRecords(anyLong(), anyLong())).thenReturn(records);

        assertFalse(strategy.checkAlert(patient));
    }

    /**
     * Tests the OxygenSaturationStrategy for detecting a drop in oxygen saturation.
     */
    @Test
    public void testOxygenSaturationStrategyOxygenSaturationDrop() {
        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 95, "Saturation", 1700000000000L),
                new PatientRecord(1, 90, "Saturation", 1700000060000L),
                new PatientRecord(1, 85, "Saturation", 1700000120000L)
        );
        when(patient.getRecords(anyLong(), anyLong())).thenReturn(records);

        assertTrue(strategy.checkAlert(patient));
    }
}
