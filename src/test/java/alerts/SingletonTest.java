package alerts;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Unit tests for singleton classes in the alerts package.
 */
public class SingletonTest {

    /**
     * Tests if only one instance of HealthDataSimulator is created.
     */
    @Test
    public void testHealthDataSimulatorSingleton() {
        HealthDataSimulator instance1 = HealthDataSimulator.getInstance();
        HealthDataSimulator instance2 = HealthDataSimulator.getInstance();
        assertSame(instance1, instance2);
    }

    /**
     * Tests if only one instance of DataStorage is created.
     */
    @Test
    public void testDataStorageSingleton() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();
        assertSame(instance1, instance2);
    }

    /**
     * Tests the functionality of the HealthDataSimulator singleton.
     */
    @Test
    public void testHealthDataSimulatorFunctionality() {
        HealthDataSimulator simulator = HealthDataSimulator.getInstance();
        assertNotNull(simulator);
        // Additional checks for simulator behavior can be added here
    }

    /**
     * Tests the functionality of the DataStorage singleton.
     */
    @Test
    public void testDataStorageFunctionality() {
        DataStorage dataStorage = DataStorage.getInstance();
        assertNotNull(dataStorage);

    }
}
