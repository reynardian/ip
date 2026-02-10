package voyager;

import voyager.exception.VoyagerException;
import voyager.task.Storage;
import voyager.ui.Parser;
import voyager.ui.Ui;

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
                    if (args.isEmpty()) {
                        throw new VoyagerException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    voyager.task.Task todo = new voyager.task.ToDo(args);
                    taskList.add(todo);
                    storage.save(taskList.getAll());
                    return ui.showTaskAdded(todo, taskList.size());

                case "deadline":
                    String[] deadlineParts = args.split("/by");
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

                case "event":
                    String[] eventParts = args.split("/from|/to");
                    assert eventParts != null : "Split operation on arguments should never return null";
                    if (eventParts.length < 3) {
                        throw new VoyagerException("OOPS!!! Please use: event [desc] /from [start] /to [end]");
                    }
                    voyager.task.Task event = new voyager.task.Event(
                            eventParts[0].trim(),
                            eventParts[1].trim(),
                            eventParts[2].trim());
                    taskList.add(event);
                    storage.save(taskList.getAll());
                    return ui.showTaskAdded(event, taskList.size());

                case "mark":
                    voyager.task.Task marked = taskList.mark(Integer.parseInt(args) - 1);
                    storage.save(taskList.getAll());
                    return ui.showTaskMarked(marked);

                case "unmark":
                    voyager.task.Task unmarked = taskList.unmark(Integer.parseInt(args) - 1);
                    storage.save(taskList.getAll());
                    return ui.showTaskUnmarked(unmarked);

                case "delete":
                    voyager.task.Task removed = taskList.remove(Integer.parseInt(args) - 1);
                    storage.save(taskList.getAll());
                    return ui.showTaskRemoved(removed, taskList.size());

                case "find":
                    if (args.isEmpty()) {
                        throw new VoyagerException("OOPS!!! The search keyword cannot be empty.");
                    }
                    List<voyager.task.Task> allTasks = taskList.getAll();
                    List<voyager.task.Task> matchingTasks = new ArrayList<>();
                    for (voyager.task.Task task : allTasks) {
                        if (task.getDescription().toLowerCase().contains(args.toLowerCase())) {
                            matchingTasks.add(task);
                        }
                    }
                    return ui.showFoundTasks(matchingTasks);

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
}