package com.alerts.strategies;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * A strategy class to check for oxygen saturation alerts.
 */
public class OxygenSaturationStrategy implements AlertStrategy {

    /**
     * Checks if there is an oxygen saturation alert for the given patient.
     * @param patient The patient object containing the records.
     * @return True if there is an oxygen saturation alert, otherwise false.
     */
    @Override
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(1700000000000L, 1800000000000L);
        PatientRecord previousRecord = null;

        for (PatientRecord record : records) {
            String recordType = record.getRecordType();
            double measurementValue = record.getMeasurementValue();

            if (recordType.equals("Saturation")) {
                if (measurementValue < 92) {
                    return true;
                }
                if (previousRecord != null && previousRecord.getRecordType().equals("Saturation")) {
                    long timeDifference = record.getTimestamp() - previousRecord.getTimestamp();
                    double valueDifference = previousRecord.getMeasurementValue() - measurementValue;

                    if (timeDifference <= 1000 * 60 * 10 && valueDifference >= 5) {
                        return true;
                    }
                }
            }
            previousRecord = record;
        }
        return false;
    }
}
