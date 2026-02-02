package voyager.task;

import voyager.exception.VoyagerException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of tasks and provides operations to modify it.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list with existing tasks.
     *
     * @param tasks Existing tasks.
     */
    public TaskList(List<voyager.task.Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns all tasks.
     *
     * @return Task list.
     */
    public List<voyager.task.Task> getAll() {
        return tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void add(voyager.task.Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task at the given index.
     *
     * @param index Index to remove (0-based).
     * @return Removed task.
     * @throws VoyagerException If index is invalid.
     */
    public voyager.task.Task remove(int index) throws VoyagerException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /**
     * Marks a task as done.
     *
     * @param index Index to mark (0-based).
     * @return Marked task.
     * @throws VoyagerException If index is invalid.
     */
    public voyager.task.Task mark(int index) throws VoyagerException {
        checkIndex(index);
        voyager.task.Task task = tasks.get(index);
        task.mark();
        return task;
    }

    /**
     * Unmarks a task as not done.
     *
     * @param index Index to unmark (0-based).
     * @return Unmarked task.
     * @throws VoyagerException If index is invalid.
     */
    public voyager.task.Task unmark(int index) throws VoyagerException {
        checkIndex(index);
        voyager.task.Task task = tasks.get(index);
        task.unmark();
        return task;
    }

    /**
     * Returns number of tasks.
     *
     * @return Task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks if index is valid.
     *
     * @param index Index to check.
     * @throws VoyagerException If index is out of bounds.
     */
    private void checkIndex(int index) throws VoyagerException {
        if (index < 0 || index >= tasks.size()) {
            throw new VoyagerException("OOPS!!! Invalid task number.");
        }
    }
}
