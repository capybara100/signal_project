package com.cardio_generator.outputs;
/**
* This interface is charge of how the data is outputed
*/
public interface OutputStrategy {
    /**
    * Selects how the data is ouputted
    * @param patientId is a int that identifices the patient
    * @param timestamp is a long that is the recorded time
    * @param label is a String that labels the data
    * @param data is a String that is the data
    */
    void output(int patientId, long timestamp, String label, String data);
}
