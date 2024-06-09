package com.alerts.factories;

import com.alerts.Alert;

/**
 * Abstract factory class for creating alerts.
 */
public abstract class AlertFactory {

    /**
     * Creates an alert for the given patient.
     * @param patientId The ID of the patient.
     * @param condition The condition associated with the alert.
     * @param timestamp The timestamp when the alert occurred.
     * @return An instance of Alert.
     */
    public abstract Alert createAlert(String patientId, String condition, long timestamp);
}
