package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.data_management.DataStorage;
import com.data_management.DataReaderImplementation;
import com.data_management.PatientRecord;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Unit tests for the DataReaderImplementation class.
 */
class DataReaderTest {
    @BeforeEach
    void setUp() {
        DataStorage.resetInstance();
    }
    /**
     * Tests the reading of data from files and verifies the stored data.
     */
    @Test
    void testDataReading() throws IOException {
        // Prepare test data directory
        Path testDataDirectory = Files.createTempDirectory("testData");
        Path testFile1 = Paths.get(testDataDirectory.toString(), "testFile1.txt");
        Path testFile2 = Paths.get(testDataDirectory.toString(), "testFile2.txt");

        // Write test data to files
        Files.write(testFile1, "1,100.0,WhiteBloodCells,1714376789050\n2,200.0,RedBloodCells,1714376789051\n".getBytes());
        Files.write(testFile2, "3,300.0,WhiteBloodCells,1714376789052\n4,400.0,RedBloodCells,1714376789053\n".getBytes());

        DataStorage dataStorage =  DataStorage.getInstance();
        DataReaderImplementation reader = new DataReaderImplementation(testDataDirectory);

        reader.readData(dataStorage);

        // Validate the data stored in DataStorage
        List<PatientRecord> records1 = dataStorage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(1, records1.size());
        assertEquals(100.0, records1.get(0).getMeasurementValue());

        List<PatientRecord> records2 = dataStorage.getRecords(2, 1714376789050L, 1714376789051L);
        assertEquals(1, records2.size());
        assertEquals(200.0, records2.get(0).getMeasurementValue());

        List<PatientRecord> records3 = dataStorage.getRecords(3, 1714376789052L, 1714376789053L);
        assertEquals(1, records3.size());
        assertEquals(300.0, records3.get(0).getMeasurementValue());

        List<PatientRecord> records4 = dataStorage.getRecords(4, 1714376789052L, 1714376789053L);
        assertEquals(1, records4.size());
        assertEquals(400.0, records4.get(0).getMeasurementValue());
    }

    /**
     * Tests the behavior of the data reader when the directory is empty.
     */
    @Test
    void testEmptyDirectory() throws IOException {
        // Prepare an empty test data directory
        Path emptyDirectory = Files.createTempDirectory("emptyDir");

        DataStorage dataStorage = DataStorage.getInstance();
        DataReaderImplementation reader = new DataReaderImplementation(emptyDirectory);

        reader.readData(dataStorage);
        // Ensure no data is stored in DataStorage
        assertTrue(dataStorage.getRecords(0, 0L, Long.MAX_VALUE).isEmpty());
    }

    /**
     * Tests the behavior of the data reader with invalid data entries.
     */
    @Test
    void testInvalidData() throws IOException {
        // Prepare test data directory with invalid data
        Path testDataDirectory = Files.createTempDirectory("invalidData");
        Path testFile = Paths.get(testDataDirectory.toString(), "testFile.txt");

        // Write invalid test data to file
        Files.write(testFile, "1,0.0,WhiteBloodCells,1714376789050\n2,200.0,RedBloodCells,1714376789051\n".getBytes());

        DataStorage dataStorage =DataStorage.getInstance();
        DataReaderImplementation reader = new DataReaderImplementation(testDataDirectory);

        reader.readData(dataStorage);

        // Ensure only valid data is stored in DataStorage
        List<PatientRecord> records = dataStorage.getRecords(2, 1714376789050L, 1714376789051L);
        assertEquals(1, records.size());
        assertEquals(2, records.get(0).getPatientId());
        assertEquals(200.0, records.get(0).getMeasurementValue());
    }

    /**
     * Tests the reading of data from multiple files and verifies the stored data.
     */
    @Test
    void testMultipleFiles() throws IOException {
        // Prepare test data directory with multiple files
        Path testDataDirectory = Files.createTempDirectory("multipleFiles");
        Path testFile1 = Paths.get(testDataDirectory.toString(), "testFile1.txt");
        Path testFile2 = Paths.get(testDataDirectory.toString(), "testFile2.txt");

        Files.write(testFile1, "1,100.0,WhiteBloodCells,1714376789050\n2,200.0,RedBloodCells,1714376789051\n".getBytes());
        Files.write(testFile2, "3,300.0,WhiteBloodCells,1714376789052\n4,400.0,RedBloodCells,1714376789053\n".getBytes());

        DataStorage dataStorage =DataStorage.getInstance();
        DataReaderImplementation reader = new DataReaderImplementation(testDataDirectory);

        reader.readData(dataStorage);

        // Validate the data stored in DataStorage
        List<PatientRecord> records1 = dataStorage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(1, records1.size());
        assertEquals(100.0, records1.get(0).getMeasurementValue());

        List<PatientRecord> records2 = dataStorage.getRecords(2, 1714376789050L, 1714376789051L);
        assertEquals(1, records2.size());
        assertEquals(200.0, records2.get(0).getMeasurementValue());

        List<PatientRecord> records3 = dataStorage.getRecords(3, 1714376789052L, 1714376789053L);
        assertEquals(1, records3.size());
        assertEquals(300.0, records3.get(0).getMeasurementValue());

        List<PatientRecord> records4 = dataStorage.getRecords(4, 1714376789052L, 1714376789053L);
        assertEquals(1, records4.size());
        assertEquals(400.0, records4.get(0).getMeasurementValue());
    }

    /**
     * Tests the behavior of the data reader with an empty file.
     */
    @Test
    void testEmptyFile() throws IOException {
        // Prepare test data directory with an empty file
        Path testDataDirectory = Files.createTempDirectory("emptyFile");
        Path testFile = Paths.get(testDataDirectory.toString(), "testFile.txt");

        // Write empty test data to file
        Files.write(testFile, "".getBytes());

        DataStorage dataStorage =DataStorage.getInstance();
        DataReaderImplementation reader = new DataReaderImplementation(testDataDirectory);

        reader.readData(dataStorage);
        // Ensure no data is stored in DataStorage
        assertTrue(dataStorage.getRecords(0, 0L, Long.MAX_VALUE).isEmpty());
    }
}