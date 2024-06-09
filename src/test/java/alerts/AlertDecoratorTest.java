package alerts;

import com.alerts.Alert;
import com.alerts.decorators.PriorityAlertDecorator;
import com.alerts.decorators.RepeatedAlertDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the alert decorators.
 */
public class AlertDecoratorTest {

    /**
     * Tests the PriorityAlertDecorator.
     */
    @Test
    public void testPriorityAlertDecorator() {
        Alert baseAlert = new Alert("1", "High Blood Pressure", System.currentTimeMillis()) {
            @Override
            public void triggerAlert() {
                System.out.println("Base alert triggered.");
            }
        };
        PriorityAlertDecorator priorityAlert = new PriorityAlertDecorator(baseAlert, "High Priority");

        priorityAlert.triggerAlert();

        assertEquals("High Priority", priorityAlert.getPriority());
        assertEquals(baseAlert.getPatientId(), priorityAlert.getPatientId());
        assertEquals(baseAlert.getCondition(), priorityAlert.getCondition());
    }

    /**
     * Tests the RepeatedAlertDecorator.
     */
    @Test
    public void testRepeatedAlertDecorator() {
        Alert baseAlert = new Alert("1", "High Blood Pressure", System.currentTimeMillis()) {
            @Override
            public void triggerAlert() {
                System.out.println("Base alert triggered.");
            }
        };
        RepeatedAlertDecorator repeatedAlert = new RepeatedAlertDecorator(baseAlert, 3, 1000);

        repeatedAlert.triggerAlert();

        assertEquals(baseAlert.getPatientId(), repeatedAlert.getPatientId());
        assertTrue(repeatedAlert.getCondition().contains("repeated 3 times"));
    }

    /**
     * Tests a combination of decorators.
     */
    @Test
    public void testCombinedDecorators() {
        Alert baseAlert = new Alert("1", "High Blood Pressure", System.currentTimeMillis()) {
            @Override
            public void triggerAlert() {
                System.out.println("Base alert triggered.");
            }
        };
        Alert combinedAlert = new RepeatedAlertDecorator(new PriorityAlertDecorator(baseAlert, "High Priority"), 2, 1000);

        combinedAlert.triggerAlert();

        assertEquals(baseAlert.getPatientId(), combinedAlert.getPatientId());
        assertTrue(combinedAlert.getCondition().contains("repeated 2 times"));
    }
}
