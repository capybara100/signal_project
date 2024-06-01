package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataReader;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import org.mockito.Mockito;

import java.util.List;

/**
 * Unit tests for the DataStorage class.
 */
class DataStorageTest {

    /**
     * Tests adding and retrieving records from the DataStorage.
     */
    @Test
    void testAddAndGetRecords() {
        // Create a mock DataReader
        DataReader reader = Mockito.mock(DataReader.class);
        DataStorage storage = new DataStorage();

        // Add test data to DataStorage
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        // Retrieve and validate records
        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
        assertEquals(200.0, records.get(1).getMeasurementValue()); // Validate second record
    }
}

