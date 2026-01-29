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
 * Main entry point for the Voyager task management program.
 */
public class Voyager {
    private static final String LINE = "_".repeat(60);
    private static final String DATA_FOLDER = "data";
    private static final String DATA_FILE = DATA_FOLDER + File.separator + "voyager.txt";

    /**
     * Runs the Voyager command loop.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Task> tasks = new ArrayList<>();
        loadTasks(tasks);

        System.out.println(LINE);
        System.out.println("Hello! I'm Voyager");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        while (true) {
            String input = scanner.nextLine();
            System.out.println(LINE);

            try {
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(LINE);
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    listTasks(tasks);
                } else if (input.toLowerCase().startsWith("mark ")) {
                    markTask(tasks, input);
                } else if (input.toLowerCase().startsWith("unmark ")) {
                    unmarkTask(tasks, input);
                } else if (input.toLowerCase().startsWith("delete ")) {
                    deleteTask(tasks, input);
                } else if (input.toLowerCase().startsWith("todo ")) {
                    addToDo(tasks, input);
                } else if (input.equalsIgnoreCase("todo")) {
                    throw new VoyagerException("OOPS!!! The description of a todo cannot be empty.");
                } else if (input.toLowerCase().startsWith("deadline ")) {
                    addDeadline(tasks, input);
                } else if (input.toLowerCase().startsWith("event ")) {
                    addEvent(tasks, input);
                } else {
                    throw new VoyagerException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (VoyagerException e) {
                System.out.println(e.getMessage());
            }
        }

        scanner.close();
    }

    /** Lists all tasks in the task list. */
    private static void listTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty.");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
        }
    }

    /** Adds a todo task to the list. */
    private static void addToDo(List<Task> tasks, String input) throws VoyagerException {
        String desc = input.substring(5).trim();
        if (desc.isEmpty()) {
            throw new VoyagerException("OOPS!!! The description of a todo cannot be empty.");
        }
        Task todo = new ToDo(desc);
        tasks.add(todo);
        saveTasks(tasks);

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + todo);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /** Adds a deadline task to the list. */
    private static void addDeadline(List<Task> tasks, String input)
            throws VoyagerException {
        try {
            String[] parts = input.substring(9).split("/by");
            String desc = parts[0].trim();
            String dateString = parts[1].trim();

            if (desc.isEmpty() || dateString.isEmpty()) {
                throw new VoyagerException(
                        "OOPS!!! Deadline must have a description and a date.");
            }

            LocalDate by = LocalDate.parse(dateString);

            Task deadline = new Deadline(desc, by);
            tasks.add(deadline);
            saveTasks(tasks);

            System.out.println("Got it. I've added this task:");
            System.out.println("  " + deadline);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");

        } catch (DateTimeParseException e) {
            throw new VoyagerException(
                    "OOPS!!! Please use date format yyyy-mm-dd.");
        } catch (Exception e) {
            throw new VoyagerException(
                    "OOPS!!! Please use the format: deadline [description] /by yyyy-mm-dd");
        }
    }

    /** Adds an event task to the list. */
    private static void addEvent(List<Task> tasks, String input) throws VoyagerException {
        try {
            String[] parts = input.substring(6).split("/from|/to");
            String desc = parts[0].trim();
            String from = parts[1].trim();
            String to = parts[2].trim();

            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new VoyagerException("OOPS!!! Event must have a description, /from and /to.");
            }

            Task event = new Event(desc, from, to);
            tasks.add(event);
            saveTasks(tasks);

            System.out.println("Got it. I've added this task:");
            System.out.println("  " + event);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            throw new VoyagerException("OOPS!!! Please use the format: event [description] /from [start] /to [end]");
        }
    }

    /** Marks a specific task as done by index. */
    private static void markTask(List<Task> tasks, String input) throws VoyagerException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new VoyagerException("OOPS!!! Invalid task number to mark.");
            }
            tasks.get(index).mark();
            saveTasks(tasks);
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks.get(index));
        } catch (Exception e) {
            throw new VoyagerException("OOPS!!! Please provide a valid task number after 'mark'.");
        }
    }

    /** Unmarks a specific task as not done by index. */
    private static void unmarkTask(List<Task> tasks, String input) throws VoyagerException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new VoyagerException("OOPS!!! Invalid task number to unmark.");
            }
            tasks.get(index).unmark();
            saveTasks(tasks);
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks.get(index));
        } catch (Exception e) {
            throw new VoyagerException("OOPS!!! Please provide a valid task number after 'unmark'.");
        }
    }

    /** Deletes a task at the given index. */
    private static void deleteTask(List<Task> tasks, String input) throws VoyagerException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new VoyagerException("OOPS!!! Invalid task number to delete.");
            }

            Task removed = tasks.remove(index);
            saveTasks(tasks);
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + removed);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            throw new VoyagerException("OOPS!!! Please provide a valid task number after 'delete'.");
        }
    }

    /**
     * Loads tasks from the data file into the given task list.
     * Creates the data folder and file if they do not yet exist.
     *
     * @param tasks Task list to populate.
     */
    private static void loadTasks(List<Task> tasks) {
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
                        LocalDate by = LocalDate.parse(parts[3]);
                        task = new Deadline(desc, by);
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
    private static void saveTasks(List<Task> tasks) {
        try {
            FileWriter writer = new FileWriter(DATA_FILE);

            for (Task task : tasks) {
                writer.write(encodeTask(task)
                        + System.lineSeparator());
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving tasks.");
        }
    }

    /**
     * Converts a task into its file storage representation.
     *
     * @param task Task to encode.
     * @return Encoded task string.
     */
    private static String encodeTask(Task task) {
        String status =
                task.getStatusIcon().equals("[X]") ? "1" : "0";

        if (task instanceof ToDo) {
            return "T | " + status + " | "
                    + task.getDescription();
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

