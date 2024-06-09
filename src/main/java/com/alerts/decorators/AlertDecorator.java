package com.alerts.decorators;

import com.alerts.Alert;

/**
 * Base class for alert decorators.
 */
public abstract class AlertDecorator extends Alert {

    protected Alert decoratedAlert;

    /**
     * Constructs an AlertDecorator object.
     * @param decoratedAlert The alert to be decorated.
     */
    public AlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition(), decoratedAlert.getTimestamp());
        this.decoratedAlert = decoratedAlert;
    }

    /**
     * Triggers the alert.
     */
    @Override
    public void triggerAlert(){
        super.triggerAlert();
    }
}
