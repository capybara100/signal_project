package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;
/**
* This class generates alerts for patients
*/
public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    // Changed field name to lowerCamelCase
    private boolean[] alertStates; // false = resolved, true = pressed
    /**
    * This constructor intializes the alertStates array
    * @param patientCount is an int of th number of patients
    */
    public AlertGenerator(int patientCount) {
        // Changed field name to lowerCamelCase
        alertStates = new boolean[patientCount + 1];
    }
     /**
    * This method generates alert data for the patient using the selected ouputStrategy
    * @param patientId is an int which is the identification of the patient of the alert
    * @param outputStrategy is an OutputStrategy that is selected to be printed
    */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Changed to field name to lowerCamelCase
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                     // Changed to field name to lowerCamelCase
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Changed Lambda to lowerCamelCase as it is a variable
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                // Changed Lambda to lowerCamelCase as it is a variable
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    // Changed to field name to lowerCamelCase
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
