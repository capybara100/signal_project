package com.alerts.strategies;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * A strategy class to check for blood pressure alerts.
 */
public class BloodPressureStrategy implements AlertStrategy {

    /**
     * Checks if there is a blood pressure alert for the given patient.
     * @param patient The patient object containing the records.
     * @return True if there is a blood pressure alert, otherwise false.
     */
    @Override
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(1700000000000L, 1800000000000L);

        return checkPressure(records, "SystolicPressure", 90, 180) || checkPressure(records, "DiastolicPressure", 60, 120);
    }

    private boolean checkPressure(List<PatientRecord> records, String type, double min, double max) {
        List<PatientRecord> lastThreeRecords = new ArrayList<>();
        int count = 0;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals(type)) {
                if (record.getMeasurementValue() > max || record.getMeasurementValue() < min) {
                    return true;
                }
                lastThreeRecords.add(record);
                count++;
                if (count == 3) {
                    break;
                }
            }
        }

        if (count == 3) {
            return Math.abs(lastThreeRecords.get(0).getMeasurementValue() - lastThreeRecords.get(2).getMeasurementValue()) > 10;
        }

        return false;
    }
}
