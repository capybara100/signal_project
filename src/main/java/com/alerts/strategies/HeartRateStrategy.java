package com.alerts.strategies;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * A strategy class to check for heart rate alerts.
 */
public class HeartRateStrategy implements AlertStrategy {

    /**
     * Checks if there is a heart rate alert for the given patient.
     * @param patient The patient object containing the records.
     * @return True if there is a heart rate alert, otherwise false.
     */
    @Override
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(1700000000000L, 1800000000000L);
        PatientRecord previousRecord = null;

        for (PatientRecord record : records) {
            String recordType = record.getRecordType();
            double measurementValue = record.getMeasurementValue();

            if (recordType.equals("HeartRate")) {
                if (measurementValue < 50 || measurementValue > 100) {
                    return true;
                }
            }
            if (previousRecord != null && previousRecord.getRecordType().equals("HeartRate")) {
                if (Math.abs(measurementValue - previousRecord.getMeasurementValue()) > 10) {
                    return true;
                }
            }
            previousRecord = record;
        }
        return false;
    }
}
