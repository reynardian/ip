package voyager.exception;

/**
 * Custom exception class for Voyager errors.
 */
public class VoyagerException extends Exception {

    /**
     * Creates a VoyagerException with a message.
     *
     * @param message Error message.
     */
    public VoyagerException(String message) {
        super(message);
    }
}
