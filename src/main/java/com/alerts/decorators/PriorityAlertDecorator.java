package com.alerts.decorators;

import com.alerts.Alert;

/**
 * A decorator class to add priority information to alerts.
 */
public class PriorityAlertDecorator extends AlertDecorator {

    private String priority;

    /**
     * Constructs a PriorityAlertDecorator object.
     * @param decoratedAlert The alert to be decorated.
     * @param priority The priority level of the alert.
     */
    public PriorityAlertDecorator(Alert decoratedAlert, String priority) {
        super(decoratedAlert);
        this.priority = priority;
    }

    /**
     * Triggers the alert and displays its priority.
     */
    @Override
    public void triggerAlert(){
        super.triggerAlert();
        System.out.println(priority);
    }

    /**
     * Retrieves the priority of the alert.
     * @return The priority of the alert.
     */
    public String getPriority() {
        return priority;
    }
}
