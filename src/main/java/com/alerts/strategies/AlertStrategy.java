package com.alerts.strategies;

import com.data_management.Patient;

/**
 * An interface for defining alert strategy.
 */
public interface AlertStrategy {

    /**
     * Checks for an alert based on the implemented strategy.
     *
     * @param patient The patient object for which the alert is being checked.
     * @return True if the alert condition is met, otherwise false.
     */
    boolean checkAlert(Patient patient);
}
