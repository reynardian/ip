package voyager.task;

/**
 * Represents a generic task that can be managed by the application.
 * A Task contains a description and a status tracking whether it is completed.
 */
public class Task {

    private String description;
    private boolean isDone;

    /**
     * Initializes a new task with the given description.
     * The task is initially marked as not done.
     *
     * @param description The text description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** * Marks the task as completed.
     */
    public void mark() {
        this.isDone = true;
    }

    /** * Marks the task as not completed.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns the description of the task.
     * * @return The task description string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the task has been completed.
     * * @return True if marked as done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the status icon of the task.
     *
     * @return "[X]" if the task is done, otherwise "[ ]".
     */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Returns a string representation of the task.
     * The string includes the status icon and the description.
     * * @return A formatted string representing the task.
     */
    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}