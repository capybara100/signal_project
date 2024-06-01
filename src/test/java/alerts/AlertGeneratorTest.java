package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.cardio_generator.generators.AlertGenerator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

class AlertGeneratorTest {

    @Test
    void testEvaluateData() {
        // Set up System.out to capture the print statements
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Create a sample patient with test data
        List<PatientRecord> testData = new ArrayList<>();
        testData.add(new PatientRecord(1, 120, "SystolicPressure", 1));
        testData.add(new PatientRecord(1, 80, "DiastolicPressure", 2));
        testData.add(new PatientRecord(1, 95, "Saturation", 3));
        testData.add(new PatientRecord(1, 45, "ECG", 4));

        Patient patient = new Patient(1);

        // Create an instance of AlertGenerator
        AlertGenerator alertGenerator = new AlertGenerator(1);

        // Call the evaluateData method
        alertGenerator.evaluateData(patient);

        // Retrieve the triggered alerts from the captured output
        String output = outContent.toString();
        System.setOut(originalOut); // Reset System.out to its original state

        // Assert that the correct alerts are triggered
        assertTrue(output.contains("Critical pressure detected"));
        assertTrue(output.contains("Low blood saturation detected"));
        assertTrue(output.contains("Abnormal heart rate detected"));
        assertTrue(output.contains("Irregular heart beat detected"));
    }
}
