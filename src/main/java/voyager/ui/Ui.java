package voyager.ui;

import voyager.task.Task;
import java.util.List;

/**
 * Handles user interface interactions for the Voyager application.
 * In GUI mode, these methods return Strings to be displayed in the chat interface.
 */
public class Ui {
    /**
     * Returns the opening welcome message.
     */
    public String showWelcome() {
        return "Hello! I'm Voyager\nWhat can I do for you?";
    }

    /**
     * Returns the closing goodbye message.
     */
    public String showGoodbye() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Returns an error message.
     */
    public String showError(String message) {
        return message;
    }

    /**
     * Returns a general message.
     */
    public String showMessage(String message) {
        return message;
    }

    /**
     * Confirms a task was added and returns the message as a String.
     */
    public String showTaskAdded(Task task, int size) {
        return "Got it. I've added this task:\n  " + task +
                "\nNow you have " + size + " tasks in the list.";
    }

    /**
     * Confirms a task was removed.
     */
    public String showTaskRemoved(Task task, int size) {
        return "Noted. I've removed this task:\n  " + task +
                "\nNow you have " + size + " tasks in the list.";
    }

    /**
     * Confirms a task was marked as done.
     */
    public String showTaskMarked(Task task) {
        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Confirms a task was unmarked.
     */
    public String showTaskUnmarked(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Returns the entire list of tasks as a single formatted String.
     */
    public String showList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return "Your task list is empty.";
        }

        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(".").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns the matching search results as a single formatted String.
     */
    public String showFoundTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return "No matching tasks found in your list.";
        }

        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(".").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
