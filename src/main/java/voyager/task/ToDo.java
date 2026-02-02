package voyager.task;

/**
 * Represents a todo task.
 */
public class ToDo extends voyager.task.Task {

    /**
     * Creates a todo task.
     *
     * @param description Task description.
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
