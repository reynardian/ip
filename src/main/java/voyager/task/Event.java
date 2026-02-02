package voyager.task;

/**
 * Represents an event task with a specific start and end time.
 * An Event object corresponds to a task that happens during a
 * specific time frame.
 */
public class Event extends voyager.task.Task {

    private String from;
    private String to;

    /**
     * Initializes a new event task with a description and a time range.
     *
     * @param description The description of the event.
     * @param from The start time/date of the event.
     * @param to The end time/date of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start time of the event.
     *
     * @return The start time string.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the end time of the event.
     *
     * @return The end time string.
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns a string representation of the event task.
     * The string includes the task type [E], the status icon, the description,
     * and the time range (from and to).
     *
     * @return A formatted string representing the event task.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}