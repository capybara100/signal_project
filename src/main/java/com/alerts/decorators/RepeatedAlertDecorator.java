package com.alerts.decorators;

import com.alerts.Alert;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A decorator class to add repetition functionality to alerts.
 */
public class RepeatedAlertDecorator extends AlertDecorator {

    private static final Logger logger = Logger.getLogger(RepeatedAlertDecorator.class.getName());
    private final int repeatCount;
    private final long interval;

    /**
     * Constructs a RepeatedAlertDecorator object.
     * @param decoratedAlert The alert to be decorated.
     * @param repeatCount The number of times the alert should be repeated.
     * @param interval The interval between each repetition.
     */
    public RepeatedAlertDecorator(Alert decoratedAlert, int repeatCount, long interval) {
        super(decoratedAlert);
        this.repeatCount = repeatCount;
        this.interval = interval;
    }

    /**
     * Triggers the alert and repeats it as specified.
     */
    @Override
    public void triggerAlert() {
        for (int i = 0; i < repeatCount; i++) {
            logger.info("Repeated Alert: " + getCondition() + " for Patient ID: " + getPatientId() + " at " + getTimestamp());
            decoratedAlert.triggerAlert(); // Call the sendAlert method of the decorated alert

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Interrupted Exception during alert repetition", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Retrieves the condition of the alert along with information about repetition.
     * @return The condition of the alert along with repetition information.
     */
    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " (repeated " + repeatCount + " times)";
    }
}
