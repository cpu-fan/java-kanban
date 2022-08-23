package tasktracker.exceptions;

public class TaskTimeValidationException extends RuntimeException {
    public TaskTimeValidationException(String message) {
        super(message);
    }

    public TaskTimeValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
