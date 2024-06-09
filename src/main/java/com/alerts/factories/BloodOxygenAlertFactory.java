package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.BloodOxygenAlert;

/**
 * Factory class for creating BloodOxygenAlert instances.
 */
public class BloodOxygenAlertFactory extends AlertFactory {

    /**
     * Creates a BloodOxygenAlert for the given parameters.
     * @param patientId The ID of the patient.
     * @param condition The condition associated with the alert.
     * @param timestamp The timestamp when the alert occurred.
     * @return An instance of BloodOxygenAlert.
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}
