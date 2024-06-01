package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The AlertGenerator class is responsible for evaluating patient data and triggering alerts
 * based on specific conditions such as blood pressure, oxygen saturation, ECG data, and combined conditions.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private List<Alert> triggeredAlerts;

    /**
     * Constructor to initialize AlertGenerator with a DataStorage instance.
     *
     * @param dataStorage the DataStorage instance used to fetch patient data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.triggeredAlerts = new ArrayList<>();
    }

    /**
     * Evaluates the data for a given patient and triggers alerts if conditions are met.
     *
     * @param patient the patient whose data is to be evaluated
     */
    public void evaluateData(Patient patient) {
        // Retrieve records for the specified time range
        List<PatientRecord> patientRecords = patient.getRecords(1700000000000L, 1800000000000L);
        evaluateBloodPressure(patientRecords);
        evaluateOxygenSaturation(patientRecords);
        evaluateHypotensiveHypoxemia(patientRecords);
        evaluateECG(patientRecords);
    }

    /**
     * Evaluates blood pressure data and triggers alerts based on trends and thresholds.
     *
     * @param patientRecords the list of patient records to evaluate
     */
    private void evaluateBloodPressure(List<PatientRecord> patientRecords) {
        List<PatientRecord> patientsBloodPressure = patientRecords.stream()
                .filter(x -> x.getRecordType().equals("DiastolicPressure") || x.getRecordType().equals("SystolicPressure"))
                .collect(Collectors.toList());

        List<PatientRecord> patientsDiastolicPressure = patientRecords.stream()
                .filter(x -> x.getRecordType().equals("DiastolicPressure"))
                .collect(Collectors.toList());

        List<PatientRecord> patientsSystolicPressure = patientRecords.stream()
                .filter(x -> x.getRecordType().equals("SystolicPressure"))
                .collect(Collectors.toList());

        // Trend Alert: Check for increasing or decreasing trends
        for (int i = 0; i < patientsBloodPressure.size() - 2; i++) {
            if (patientsBloodPressure.get(i + 1).getMeasurementValue() > patientsBloodPressure.get(i).getMeasurementValue() + 10
                    && patientsBloodPressure.get(i + 2).getMeasurementValue() > patientsBloodPressure.get(i + 1).getMeasurementValue() + 10) {
                Alert alert = new Alert(String.valueOf(patientsBloodPressure.get(i).getPatientId()), "triggered Increasing Blood Pressure Trend Problem", System.currentTimeMillis());
                triggerAlert(alert);
            }
            if (patientsBloodPressure.get(i + 1).getMeasurementValue() < patientsBloodPressure.get(i).getMeasurementValue() - 10
                    && patientsBloodPressure.get(i + 2).getMeasurementValue() < patientsBloodPressure.get(i + 1).getMeasurementValue() - 10) {
                Alert alert = new Alert(String.valueOf(patientsBloodPressure.get(i).getPatientId()), "triggered Decreasing Blood Pressure Trend Problem", System.currentTimeMillis());
                triggerAlert(alert);
            }
        }

        // Threshold Alert: Check for critical thresholds
        for (PatientRecord patientRecord : patientsDiastolicPressure) {
            if (patientRecord.getMeasurementValue() > 120) {
                Alert alert = new Alert(String.valueOf(patientRecord.getPatientId()), "triggered Diastolic Pressure higher than 120", System.currentTimeMillis());
                triggerAlert(alert);
            }
            if (patientRecord.getMeasurementValue() < 60) {
                Alert alert = new Alert(String.valueOf(patientRecord.getPatientId()), "triggered Diastolic Pressure lower than 60", System.currentTimeMillis());
                triggerAlert(alert);
            }
        }

        for (PatientRecord patientRecord : patientsSystolicPressure) {
            if (patientRecord.getMeasurementValue() > 180) {
                Alert alert = new Alert(String.valueOf(patientRecord.getPatientId()), "triggered Systolic Pressure higher than 180", System.currentTimeMillis());
                triggerAlert(alert);
            }
            if (patientRecord.getMeasurementValue() < 90) {
                Alert alert = new Alert(String.valueOf(patientRecord.getPatientId()), "triggered Systolic Pressure lower than 90", System.currentTimeMillis());
                triggerAlert(alert);
            }
        }
    }

    /**
     * Evaluates oxygen saturation levels and triggers alerts based on thresholds and rapid drops.
     *
     * @param patientRecords the list of patient records to evaluate
     */
    private void evaluateOxygenSaturation(List<PatientRecord> patientRecords) {
        List<PatientRecord> patientsSaturationLevel = patientRecords.stream()
                .filter(x -> x.getRecordType().equals("Saturation"))
                .collect(Collectors.toList());

        // Low Saturation Alert
        for (PatientRecord patientRecord : patientsSaturationLevel) {
            if (patientRecord.getMeasurementValue() < 92) {
                Alert alert = new Alert(String.valueOf(patientRecord.getPatientId()), "triggered Saturation level lower than 92.0%", System.currentTimeMillis());
                triggerAlert(alert);
            }
        }

        // Rapid Drop Alert
        for (int i = 0; i < patientsSaturationLevel.size() - 1; i++) {
            if (patientsSaturationLevel.get(i + 1).getTimestamp() - patientsSaturationLevel.get(i).getTimestamp() < 600000) {
                if (patientsSaturationLevel.get(i).getMeasurementValue() - patientsSaturationLevel.get(i + 1).getMeasurementValue() >= 5) {
                    Alert alert = new Alert(String.valueOf(patientsSaturationLevel.get(i).getPatientId()), "triggered Rapid Saturation Drop Alert", System.currentTimeMillis());
                    triggerAlert(alert);
                }
            }
        }
    }

    /**
     * Evaluates combined conditions for hypotensive hypoxemia and triggers alerts.
     *
     * @param patientRecords the list of patient records to evaluate
     */
    private void evaluateHypotensiveHypoxemia(List<PatientRecord> patientRecords) {
        List<PatientRecord> patientsSaturationSystolicPressure = patientRecords.stream()
                .filter(x -> x.getRecordType().equals("Saturation") || x.getRecordType().equals("SystolicPressure"))
                .collect(Collectors.toList());

        boolean saturationAlert = false;
        boolean systolicAlert = false;

        // Check for combined condition of low saturation and low systolic pressure
        for (PatientRecord patientRecord : patientsSaturationSystolicPressure) {
            if (patientRecord.getRecordType().equals("Saturation") && patientRecord.getMeasurementValue() < 92) {
                saturationAlert = true;
            }

            if (patientRecord.getRecordType().equals("SystolicPressure") && patientRecord.getMeasurementValue() < 90) {
                systolicAlert = true;
            }

            if (saturationAlert && systolicAlert) {
                Alert alert = new Alert(String.valueOf(patientRecord.getPatientId()), "triggered Hypotensive Hypoxemia Alert", System.currentTimeMillis());
                triggerAlert(alert);
            }
        }
    }

    /**
     * Evaluates ECG data for abnormalities and triggers alerts based on heart rate and irregularities.
     *
     * @param patientRecords the list of patient records to evaluate
     */
    private void evaluateECG(List<PatientRecord> patientRecords) {
        // Filter ECG records
        List<PatientRecord> ecgRecords = patientRecords.stream()
                .filter(x -> x.getRecordType().equals("ECG"))
                .collect(Collectors.toList());

        // Process ECG records for each patient
        String currentPatientId = "";
        List<PatientRecord> currentPatientEcgRecords = new ArrayList<>();
        for (PatientRecord record : ecgRecords) {
            String patientId = String.valueOf(record.getPatientId());
            if (!patientId.equals(currentPatientId) && !currentPatientEcgRecords.isEmpty()) {
                processECGRecords(currentPatientId, currentPatientEcgRecords);
                currentPatientEcgRecords.clear();
            }
            currentPatientId = patientId;
            currentPatientEcgRecords.add(record);
        }
        if (!currentPatientEcgRecords.isEmpty()) {
            processECGRecords(currentPatientId, currentPatientEcgRecords);
        }
    }

    /**
     * Processes ECG records to calculate heart rate and detect irregularities.
     *
     * @param patientId the ID of the patient
     * @param patientEcgRecords the list of ECG records for the patient
     */
    private void processECGRecords(String patientId, List<PatientRecord> patientEcgRecords) {
        double totalRRInterval = 0.0;
        int beatCount = 0;
        List<Double> rrIntervals = new ArrayList<>();

        // Calculate RR intervals and heart rate
        for (int i = 1; i < patientEcgRecords.size(); i++) {
            double previousData = patientEcgRecords.get(i - 1).getMeasurementValue();
            double currentData = patientEcgRecords.get(i).getMeasurementValue();
            if (previousData < 0 && currentData > 0) {
                long previousTimestamp = patientEcgRecords.get(i - 1).getTimestamp();
                long currentTimestamp = patientEcgRecords.get(i).getTimestamp();
                double rrInterval = (currentTimestamp - previousTimestamp) / 1000.0; // convert ms to seconds
                rrIntervals.add(rrInterval);
                totalRRInterval += rrInterval;
                beatCount++;
            }
        }

        if (beatCount > 0) {
            double averageRRInterval = totalRRInterval / beatCount;
            double heartRate = 60 / averageRRInterval;

            // Logging for debugging
            System.out.println("Patient ID: " + patientId);
            System.out.println("Total RR Interval: " + totalRRInterval);
            System.out.println("Beat Count: " + beatCount);
            System.out.println("Average RR Interval: " + averageRRInterval);
            System.out.println("Heart Rate: " + heartRate);

            // Trigger alerts for abnormal heart rate
            if (heartRate < 50) {
                Alert alert = new Alert(patientId, "triggered Abnormal Heart Rate Lower Than 50", System.currentTimeMillis());
                triggerAlert(alert);
            }
            if (heartRate > 100) {
                Alert alert = new Alert(patientId, "triggered Abnormal Heart Rate Higher Than 100", System.currentTimeMillis());
                triggerAlert(alert);
            }

            // Calculate standard deviation of RR intervals to detect irregular beats
            double averageRRIntervalSum = rrIntervals.stream().mapToDouble(Double::doubleValue).sum() / rrIntervals.size();
            double sumOfSquaredDifferences = rrIntervals.stream().mapToDouble(interval -> Math.pow(interval - averageRRIntervalSum, 2)).sum();
            double standardDeviation = Math.sqrt(sumOfSquaredDifferences / rrIntervals.size());

            if (standardDeviation > 0.1) {
                Alert alert = new Alert(patientId, "triggered Irregular Beat Detected", System.currentTimeMillis());
                triggerAlert(alert);
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        Logger logger = Logger.getLogger(AlertGenerator.class.getName());
        logger.log(Level.INFO, alert.getPatientId() + ": " + alert.getCondition() + ", at " + alert.getTimestamp());
        // Print to console for test capture
        System.out.println(alert.getPatientId() + ": " + alert.getCondition() + ", at " + alert.getTimestamp());
    }

}
