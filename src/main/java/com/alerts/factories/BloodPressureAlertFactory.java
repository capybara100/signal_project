package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.BloodPressureAlert;

/**
 * Factory class for creating BloodPressureAlert instances.
 */
public class BloodPressureAlertFactory extends AlertFactory {

    /**
     * Creates a BloodPressureAlert for the given parameters.
     * @param patientId The ID of the patient.
     * @param condition The condition associated with the alert.
     * @param timestamp The timestamp when the alert occurred.
     * @return An instance of BloodPressureAlert.
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}
