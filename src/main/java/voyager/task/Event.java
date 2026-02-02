package voyager.task;

/**
 * Represents an event task with start and end times.
 */
public class Event extends voyager.task.Task {

    private String from;
    private String to;

    /**
     * Creates an event task.
     *
     * @param description Task description.
     * @param from        Start time.
     * @param to          End time.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

