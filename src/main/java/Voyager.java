import java.util.ArrayList;
import java.util.Scanner;

class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() { this.isDone = true; }
    public void unmark() { this.isDone = false; }
    public String getStatusIcon() { return isDone ? "[X]" : "[ ]"; }

    @Override
    public String toString() { return getStatusIcon() + " " + description; }
}

class ToDo extends Task {
    public ToDo(String description) { super(description); }
    @Override
    public String toString() { return "[T]" + super.toString(); }
}

class Deadline extends Task {
    protected String by;
    public Deadline(String description, String by) { super(description); this.by = by; }
    @Override
    public String toString() { return "[D]" + super.toString() + " (by: " + by + ")"; }
}

class Event extends Task {
    protected String from;
    protected String to;
    public Event(String description, String from, String to) { super(description); this.from = from; this.to = to; }
    @Override
    public String toString() { return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")"; }
}

class VoyagerException extends Exception {
    public VoyagerException(String message) { super(message); }
}

public class Voyager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = "_".repeat(60);
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println(line);
        System.out.println("Hello! I'm Voyager");
        System.out.println("What can I do for you?");
        System.out.println(line);

        while (true) {
            String input = scanner.nextLine();
            System.out.println(line);

            try {
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(line);
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    if (tasks.isEmpty()) {
                        System.out.println("Your task list is empty.");
                    } else {
                        System.out.println("Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println((i + 1) + "." + tasks.get(i));
                        }
                    }
                } else if (input.toLowerCase().startsWith("mark ")) {
                    markTask(tasks, input);
                } else if (input.toLowerCase().startsWith("unmark ")) {
                    unmarkTask(tasks, input);
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

    private static void addToDo(ArrayList<Task> tasks, String input) throws VoyagerException {
        String desc = input.substring(5).trim();
        if (desc.isEmpty()) throw new VoyagerException("OOPS!!! The description of a todo cannot be empty.");
        Task todo = new ToDo(desc);
        tasks.add(todo);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + todo);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void addDeadline(ArrayList<Task> tasks, String input) throws VoyagerException {
        try {
            String[] parts = input.substring(9).split("/by");
            String desc = parts[0].trim();
            String by = parts[1].trim();
            if (desc.isEmpty() || by.isEmpty()) throw new VoyagerException("OOPS!!! Deadline must have a description and a /by time.");
            Task deadline = new Deadline(desc, by);
            tasks.add(deadline);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + deadline);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            throw new VoyagerException("OOPS!!! Please use the format: deadline [description] /by [time]");
        }
    }

    private static void addEvent(ArrayList<Task> tasks, String input) throws VoyagerException {
        try {
            String[] parts = input.substring(6).split("/from|/to");
            String desc = parts[0].trim();
            String from = parts[1].trim();
            String to = parts[2].trim();
            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) throw new VoyagerException("OOPS!!! Event must have a description, /from and /to.");
            Task event = new Event(desc, from, to);
            tasks.add(event);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + event);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            throw new VoyagerException("OOPS!!! Please use the format: event [description] /from [start] /to [end]");
        }
    }

    private static void markTask(ArrayList<Task> tasks, String input) throws VoyagerException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            if (index < 0 || index >= tasks.size()) throw new VoyagerException("OOPS!!! Invalid task number to mark.");
            tasks.get(index).mark();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks.get(index));
        } catch (Exception e) {
            throw new VoyagerException("OOPS!!! Please provide a valid task number after 'mark'.");
        }
    }

    private static void unmarkTask(ArrayList<Task> tasks, String input) throws VoyagerException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            if (index < 0 || index >= tasks.size()) throw new VoyagerException("OOPS!!! Invalid task number to unmark.");
            tasks.get(index).unmark();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks.get(index));
        } catch (Exception e) {
            throw new VoyagerException("OOPS!!! Please provide a valid task number after 'unmark'.");
        }
    }
}
