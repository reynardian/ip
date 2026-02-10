package voyager;

import voyager.exception.VoyagerException;
import voyager.task.Event;
import voyager.task.Storage;
import voyager.task.Task;
import voyager.task.ToDo;
import voyager.ui.Parser;
import voyager.ui.Ui;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

/**
 * Main entry point for the Voyager task management program.
 * Orchestrates the user interface, storage, and task list operations.
 */
public class Voyager {
    private final voyager.task.TaskList taskList;
    private final Storage storage;
    private final Ui ui;

    private static final String DEADLINE_DELIMITER = "/by";
    private static final String EVENT_FROM_DELIMITER = "/from";
    private static final String EVENT_TO_DELIMITER = "/to";

    /**
     * Initializes a new Voyager session.
     * Sets up the UI, storage, and loads existing tasks from disk.
     */
    public Voyager() {
        ui = new Ui();
        storage = new Storage();
        taskList = new voyager.task.TaskList(new ArrayList<>());

        storage.loadTasks(taskList.getAll());
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param input The raw user input from the GUI text field.
     * @return The formatted response string from the chatbot.
     */
    public String getResponse(String input) {
        try {
            String command = Parser.getCommandWord(input);
            String args = Parser.getArguments(input);

            switch (command) {
                case "welcome_trigger":
                    return ui.showWelcome();

                case "bye":
                    return ui.showGoodbye();

                case "list":
                    return ui.showList(taskList.getAll());

                case "todo":
                    return handleTodo(args);

                case "deadline":
                    return handleDeadline(args);

                case "event":
                    return handleEvent(args);

                case "mark":
                    voyager.task.Task marked = taskList.mark(Integer.parseInt(args) - 1);
                    storage.save(taskList.getAll());
                    return ui.showTaskMarked(marked);

                case "unmark":
                    voyager.task.Task unmarked = taskList.unmark(Integer.parseInt(args) - 1);
                    storage.save(taskList.getAll());
                    return ui.showTaskUnmarked(unmarked);

                case "delete":
                    return handleDelete(args);

                case "find":
                    return handleFind(args);

                default:
                    throw new VoyagerException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
        } catch (VoyagerException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "OOPS!!! Please enter a valid task number.";
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    /**
     * Processes the 'todo' command by creating and adding a new ToDo task.
     *
     * @param args The description of the todo task.
     * @return A confirmation message that the task was added.
     * @throws VoyagerException If the description is empty.
     */
    private String handleTodo(String args) throws VoyagerException, IOException {
        if (args.isEmpty()) {
            throw new VoyagerException("OOPS!!! The description of a todo cannot be empty.");
        }
        Task todo = new ToDo(args); // Note: removed the long voyager.task prefix
        taskList.add(todo);
        storage.save(taskList.getAll());
        return ui.showTaskAdded(todo, taskList.size());
    }

    /**
     * Processes the 'deadline' command by creating and adding a new Deadline task.
     *
     * @param args The description of the deadline task.
     * @return A confirmation message that the task was added.
     * @throws VoyagerException If the description is empty.
     */
    private String handleDeadline(String args) throws VoyagerException, IOException {
        String[] deadlineParts = args.split(DEADLINE_DELIMITER);
        assert deadlineParts != null : "Split operation on arguments should never return null";
        if (deadlineParts.length < 2) {
            throw new VoyagerException("OOPS!!! Please use: deadline [desc] /by [yyyy-mm-dd]");
        }
        try {
            voyager.task.Task deadline = new voyager.task.Deadline(
                    deadlineParts[0].trim(),
                    LocalDate.parse(deadlineParts[1].trim()));
            taskList.add(deadline);
            storage.save(taskList.getAll());
            return ui.showTaskAdded(deadline, taskList.size());
        } catch (DateTimeParseException e) {
            throw new VoyagerException("OOPS!!! Please enter the date in yyyy-mm-dd format.");
        }
    }

    /**
     * Processes the 'event' command by parsing start and end times.
     *
     * @param args The input containing description, /from, and /to timings.
     * @return A confirmation message from the UI.
     * @throws VoyagerException If the required delimiters are missing.
     * @throws IOException      If saving to disk fails.
     */
    private String handleEvent(String args) throws VoyagerException, IOException { // Add IOException here
        String regex = EVENT_FROM_DELIMITER + "|" + EVENT_TO_DELIMITER;
        String[] eventParts = args.split(regex);
        assert eventParts != null : "Split operation on arguments should never return null";

        if (eventParts.length < 3) {
            throw new VoyagerException("OOPS!!! Please use: event [desc] /from [start] /to [end]");
        }

        Task event = new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        taskList.add(event);
        storage.save(taskList.getAll()); // This is the line that needs the throws clause
        return ui.showTaskAdded(event, taskList.size());
    }

    /**
     * Processes the 'delete' command by deleting the task .
     *
     * @return A confirmation message from the UI.
     * @throws VoyagerException If the required delimiters are missing.
     * @throws IOException      If saving to disk fails.
     */
    private String handleDelete(String args) throws VoyagerException, IOException {
        try {
            int index = Integer.parseInt(args) - 1;
            Task removed = taskList.remove(index);
            storage.save(taskList.getAll());
            return ui.showTaskRemoved(removed, taskList.size());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new VoyagerException("OOPS!!! Please enter a valid task number.");
        }
    }

    /**
     * Processes the 'find' command by finding the task .
     *
     * @return A confirmation message from the UI.
     * @throws VoyagerException If the required delimiters are missing.
     */
    private String handleFind(String args) throws VoyagerException {
        if (args.isEmpty()) {
            throw new VoyagerException("OOPS!!! The search keyword cannot be empty.");
        }

        List<Task> allTasks = taskList.getAll();
        List<Task> matchingTasks = new ArrayList<>();

        for (Task task : allTasks) {
            if (task.getDescription().toLowerCase().contains(args.toLowerCase())) {
                matchingTasks.add(task);
            }
        }
        return ui.showFoundTasks(matchingTasks);
    }
}