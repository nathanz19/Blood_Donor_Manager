//Nathan Zheng 116192673 CSE 214.R02

/**
 * Contains all the Exceptions for the program.
 *
 * @author Nathan Zheng
 * @version 1.0
 * @since 1.0
 */
public class PatientException extends Exception {

    /**
     * Constructs a PatientException with a given message.
     *
     * @param message the message
     */
    public PatientException(String message) {
        super(message);
    }

    /**
     * Thrown when the patient list exceeds the maximum size.
     */
    public static class FullListException extends Exception {

        /**
         * Constructs an instance of FullListException.
         *
         * @param message the message for the exception
         */
        public FullListException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a patient is given an invalid blood type.
     */
    public static class InvalidBloodTypeException extends Exception {

        /**
         * Constructs an instance of InvalidBloodTypeException.
         *
         * @param message the message for the exception
         */
        public InvalidBloodTypeException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a patient does not exist.
     */
    public static class PatientDoesNotExistException extends Exception {

        /**
         * Constructs an instance of PatientDoesNotExistException.
         *
         * @param message the message for the exception
         */
        public PatientDoesNotExistException(String message) {
            super(message);
        }
    }
}
