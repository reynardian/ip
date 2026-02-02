package voyager.ui;

import voyager.task.Task;

import java.util.List;
import java.util.Scanner;

/**
 * Handles all user interface interactions for the Voyager application.
 * Responsible for reading input from the console and displaying messages to the user.
 */
public class Ui {
    private static final String LINE = "_".repeat(60);
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the opening welcome message to the user.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm Voyager");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Displays the closing goodbye message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Reads the next line of input from the user.
     *
     * @return The full string entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints a horizontal divider line to the console.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to be displayed.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays a general message to the user.
     *
     * @param message The message to be displayed.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Confirms the successful addition of a task and displays the current list size.
     *
     * @param task The task that was added.
     * @param size The current number of tasks in the list.
     */
    public void showTaskAdded(Task task, int size) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Confirms the removal of a task and displays the current list size.
     *
     * @param task The task that was removed.
     * @param size The current number of tasks in the list.
     */
    public void showTaskRemoved(Task task, int size) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Confirms that a task has been marked as completed.
     *
     * @param task The task that was marked.
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Confirms that a task has been marked as not completed.
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Displays the entire list of tasks with their corresponding indices.
     *
     * @param tasks The list of tasks to be displayed.
     */
    public void showList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty.");
            return;
        }

        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }
}