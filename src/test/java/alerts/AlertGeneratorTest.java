package alerts;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AlertGeneratorTest {
    private AlertGenerator alertGenerator;
    private DataStorage dataStorage;

    @BeforeEach
    void setUp() {
        dataStorage = new DataStorage();
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    void testEvaluateData() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Create a sample patient with test data
        Patient patient = new Patient(1);
        patient.addRecord(190, "SystolicPressure", 1700000000001L); // High systolic pressure
        patient.addRecord(49, "HeartRate", 1700000000002L); // Low heart rate
        patient.addRecord(85, "Saturation", 1700000000003L); // Low saturation

        // Adding ECG records to simulate a low heart rate
        patient.addRecord(-0.5, "ECG", 1700000000004L); // Simulated ECG data
        patient.addRecord(0.7, "ECG", 1700000002004L); // Simulated ECG data crossing zero indicating a beat (2 seconds interval)
        patient.addRecord(-0.5, "ECG", 1700000004004L); // Simulated ECG data
        patient.addRecord(0.8, "ECG", 1700000006004L); // Simulated ECG data crossing zero indicating a beat (2 seconds interval)

        // Call the evaluateData method
        alertGenerator.evaluateData(patient);

        // Retrieve the triggered alerts from the captured output
        String output = outContent.toString();
        System.setOut(originalOut); // Reset System.out to its original state

        // Print captured output for debugging
       // System.out.println("Captured Output:");
       // System.out.println(output);

        // Assert that the correct alerts are triggered
        assertTrue(output.contains("triggered Systolic Pressure higher than 180"));
        assertTrue(output.contains("triggered Abnormal Heart Rate Lower Than 50"));
        assertTrue(output.contains("triggered Saturation level lower than 92.0%"));
    }


}
