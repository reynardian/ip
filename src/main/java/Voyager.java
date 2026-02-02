import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a generic task with a description and completion status.
 */
class Task {

    private String description;
    private boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description Task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks the task as completed. */
    public void mark() {
        this.isDone = true;
    }

    /** Marks the task as not completed. */
    public void unmark() {
        this.isDone = false;
    }

    protected String getDescription() {
        return description;
    }

    protected boolean isDone() {
        return isDone;
    }

    /**
     * Returns the status icon of the task.
     *
     * @return "[X]" if done, otherwise "[ ]".
     */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}

/**
 * Represents a todo task.
 */
class ToDo extends Task {

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

/**
 * Represents a deadline task with a due time.
 */
class Deadline extends Task {

    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");
    private LocalDate by;


    /**
     * Creates a deadline task.
     *
     * @param description Task description.
     * @param by Deadline time.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }
    public LocalDate getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}

/**
 * Represents an event task with start and end times.
 */
class Event extends Task {

    private String from;
    private String to;

    /**
     * Creates an event task.
     *
     * @param description Task description.
     * @param from Start time.
     * @param to End time.
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

/**
 * Custom exception class for Voyager errors.
 */
class VoyagerException extends Exception {

    /**
     * Creates a VoyagerException with a message.
     *
     * @param message Error message.
     */
    public VoyagerException(String message) {
        super(message);
    }
}

/**
 * Represents a list of tasks and provides operations to modify it.
 */
class TaskList {
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
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns all tasks.
     *
     * @return Task list.
     */
    public List<Task> getAll() {
        return tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task at the given index.
     *
     * @param index Index to remove (0-based).
     * @return Removed task.
     * @throws VoyagerException If index is invalid.
     */
    public Task remove(int index) throws VoyagerException {
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
    public Task mark(int index) throws VoyagerException {
        checkIndex(index);
        Task task = tasks.get(index);
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
    public Task unmark(int index) throws VoyagerException {
        checkIndex(index);
        Task task = tasks.get(index);
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

class Storage {
    private static final String DATA_FOLDER = "data";
    private static final String DATA_FILE =
            DATA_FOLDER + File.separator + "voyager.txt";

    /**
     * Loads tasks from the data file into the given task list.
     * Creates the data folder and file if they do not yet exist.
     */
    public void loadTasks(List<Task> tasks) {
        try {
            File folder = new File(DATA_FOLDER);
            if (!folder.exists()) {
                folder.mkdir();
            }

            File file = new File(DATA_FILE);
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");

                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String desc = parts[2];

                Task task;

                switch (type) {
                    case "T":
                        task = new ToDo(desc);
                        break;
                    case "D":
                        task = new Deadline(desc, LocalDate.parse(parts[3]));
                        break;
                    case "E":
                        task = new Event(desc, parts[3], parts[4]);
                        break;
                    default:
                        continue;
                }

                if (isDone) {
                    task.mark();
                }

                tasks.add(task);
            }

            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks.");
        }
    }


    /**
     * Saves all tasks in the list to the data file.
     *
     * @param tasks Task list to save.
     */
    public void save(List<Task> tasks) throws IOException {
        FileWriter writer = new FileWriter(DATA_FILE);

        for (Task task : tasks) {
            writer.write(encodeTask(task) + System.lineSeparator());
        }

        writer.close();
    }

    /**
     * Converts a task into its file storage representation.
     *
     * @param task Task to encode.
     * @return Encoded task string.
     */
    private String encodeTask(Task task) {
        String status = task.isDone() ? "1" : "0";

        if (task instanceof ToDo) {
            return "T | " + status + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return "D | " + status + " | "
                    + task.getDescription() + " | " + d.getBy();
        } else if (task instanceof Event) {
            Event e = (Event) task;
            return "E | " + status + " | "
                    + task.getDescription()
                    + " | " + e.getFrom()
                    + " | " + e.getTo();
        }

        return "";
    }
}

class Parser {
    public static String getCommandWord(String input) {
        return input.trim().split(" ")[0].toLowerCase();
    }

    public static String getArguments(String input) {
        String[] parts = input.trim().split(" ", 2);
        return parts.length < 2 ? "" : parts[1];
    }
}

class Ui {
    private static final String LINE = "_".repeat(60);
    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm Voyager");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showTaskAdded(Task task, int size) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    public void showTaskRemoved(Task task, int size) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

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

/**
 * Main entry point for the Voyager task management program.
 */
public class Voyager {
    private final TaskList taskList;
    private final Storage storage;
    private final Ui ui;

    public Voyager() {
        ui = new Ui();
        storage = new Storage();
        taskList = new TaskList(new ArrayList<>());

        storage.loadTasks(taskList.getAll());
    }

    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            ui.showLine();

            try {
                String command = Parser.getCommandWord(input);
                String args = Parser.getArguments(input);

                switch (command) {
                    case "bye":
                        ui.showGoodbye();
                        return;

                    case "list":
                        ui.showList(taskList.getAll());
                        break;

                    case "todo":
                        if (args.isEmpty()) {
                            throw new VoyagerException(
                                    "OOPS!!! The description of a todo cannot be empty.");
                        }
                        Task todo = new ToDo(args);
                        taskList.add(todo);
                        storage.save(taskList.getAll());
                        ui.showTaskAdded(todo, taskList.size());
                        break;

                    case "deadline":
                        String[] dParts = args.split("/by");
                        Task deadline = new Deadline(
                                dParts[0].trim(),
                                LocalDate.parse(dParts[1].trim()));
                        taskList.add(deadline);
                        storage.save(taskList.getAll());
                        ui.showTaskAdded(deadline, taskList.size());
                        break;

                    case "event":
                        String[] eParts = args.split("/from|/to");
                        Task event = new Event(
                                eParts[0].trim(),
                                eParts[1].trim(),
                                eParts[2].trim());
                        taskList.add(event);
                        storage.save(taskList.getAll());
                        ui.showTaskAdded(event, taskList.size());
                        break;

                    case "mark":
                        Task marked = taskList.mark(Integer.parseInt(args) - 1);
                        storage.save(taskList.getAll());
                        ui.showTaskMarked(marked);
                        break;

                    case "unmark":
                        Task unmarked = taskList.unmark(Integer.parseInt(args) - 1);
                        storage.save(taskList.getAll());
                        ui.showTaskUnmarked(unmarked);
                        break;

                    case "delete":
                        Task removed = taskList.remove(Integer.parseInt(args) - 1);
                        storage.save(taskList.getAll());
                        ui.showTaskRemoved(removed, taskList.size());
                        break;

                    default:
                        throw new VoyagerException(
                                "OOPS!!! I'm sorry, but I don't know what that means :-(");
                }

            } catch (Exception e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Voyager().run();
    }
}

