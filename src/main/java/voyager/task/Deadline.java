package voyager.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline.
 * A Deadline object corresponds to a task with a specific date
 * by which it must be completed.
 */
public class Deadline extends voyager.task.Task {

    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");
    private LocalDate by;

    /**
     * Initializes a new deadline task with the specified description and date.
     *
     * @param description Task description.
     * @param by          Deadline time.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the date the task is due.
     *
     * @return The deadline date.
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns a string representation of the deadline task.
     * The string includes the task type [D], the status icon, the description,
     * and the formatted deadline date.
     *
     * @return A formatted string representing the deadline task.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}
