package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.ECGAlert;

/**
 * Factory class for creating ECGAlert instances.
 */
public class ECGAlertFactory extends AlertFactory {

    /**
     * Creates an ECGAlert for the given parameters.
     * @param patientId The ID of the patient.
     * @param condition The condition associated with the alert.
     * @param timestamp The timestamp when the alert occurred.
     * @return An instance of ECGAlert.
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}
