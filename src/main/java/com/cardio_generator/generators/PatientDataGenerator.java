package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;
/**
* Generates specific patient data
*/
public interface PatientDataGenerator {
    /**
    * Generates patient data while using the output strategy
    * @param patientId which is an int
    * @param ouputStrategy which an OutputStrategy that is being used
    */
    void generate(int patientId, OutputStrategy outputStrategy);
}
