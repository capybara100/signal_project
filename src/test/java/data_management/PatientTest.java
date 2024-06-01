package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

/**
 * Unit tests for the Patient class.
 */
class PatientTest {

    /**
     * Tests adding and retrieving patient records.
     */
    @Test
    void testAddAndGetRecords() {
        // Create a sample patient
        Patient patient = new Patient(1);

        // Add test data as patient records
        patient.addRecord(100.0, "WhiteBloodCells", 1714376789050L);
        patient.addRecord(200.0, "WhiteBloodCells", 1714376789051L);

        // Retrieve the patient records within a large time frame to get all records
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        // Assert that the retrieved records match the test data
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
        assertEquals(200.0, records.get(1).getMeasurementValue()); // Validate second record
    }

    /**
     * Tests retrieving patient records within a specific time frame.
     */
    @Test
    void testGetRecordsWithinTimeFrame() {
        // Create a sample patient
        Patient patient = new Patient(1);

        // Add test data as patient records
        patient.addRecord(100.0, "WhiteBloodCells", 1714376789050L);
        patient.addRecord(200.0, "WhiteBloodCells", 1714376789051L);
        patient.addRecord(150.0, "WhiteBloodCells", 1714376789052L);

        // Retrieve patient records within a specific time frame
        List<PatientRecord> recordsWithinTimeFrame = patient.getRecords(1714376789051L, 1714376789052L);

        // Assert that the correct records are retrieved within the time frame
        assertEquals(2, recordsWithinTimeFrame.size()); // Check if two records are retrieved
        assertEquals(200.0, recordsWithinTimeFrame.get(0).getMeasurementValue()); // Validate first record
        assertEquals(150.0, recordsWithinTimeFrame.get(1).getMeasurementValue()); // Validate second record
    }
    @Test
    void testNoRecordsWithinTimeFrame() {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "WhiteBloodCells", 1714376789050L);
        patient.addRecord(200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = patient.getRecords(1714376789052L, 1714376789053L);
        assertTrue(records.isEmpty()); // Should return an empty list
    }

    @Test
    void testSingleRecordWithinTimeFrame() {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "WhiteBloodCells", 1714376789050L);
        patient.addRecord(200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = patient.getRecords(1714376789050L, 1714376789050L);
        assertEquals(1, records.size());
        assertEquals(100.0, records.get(0).getMeasurementValue());
    }

    @Test
    void testBoundaryConditions() {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "WhiteBloodCells", 1714376789050L);
        patient.addRecord(200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = patient.getRecords(1714376789050L, 1714376789051L);
        assertEquals(2, records.size());
        assertEquals(100.0, records.get(0).getMeasurementValue());
        assertEquals(200.0, records.get(1).getMeasurementValue());
    }

}





