package voyager;

import voyager.task.Event;
import voyager.task.Deadline;
import voyager.exception.VoyagerException;
import voyager.task.Storage;
import voyager.ui.Parser;
import voyager.ui.Ui;

import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Main entry point for the Voyager task management program.
 */
public class Voyager {
    private final voyager.task.TaskList taskList;
    private final Storage storage;
    private final Ui ui;

    public Voyager() {
        ui = new Ui();
        storage = new Storage();
        taskList = new voyager.task.TaskList(new ArrayList<>());

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
                        voyager.task.Task todo = new voyager.task.ToDo(args);
                        taskList.add(todo);
                        storage.save(taskList.getAll());
                        ui.showTaskAdded(todo, taskList.size());
                        break;

                    case "deadline":
                        String[] dParts = args.split("/by");
                        voyager.task.Task deadline = new Deadline(
                                dParts[0].trim(),
                                LocalDate.parse(dParts[1].trim()));
                        taskList.add(deadline);
                        storage.save(taskList.getAll());
                        ui.showTaskAdded(deadline, taskList.size());
                        break;

                    case "event":
                        String[] eParts = args.split("/from|/to");
                        voyager.task.Task event = new Event(
                                eParts[0].trim(),
                                eParts[1].trim(),
                                eParts[2].trim());
                        taskList.add(event);
                        storage.save(taskList.getAll());
                        ui.showTaskAdded(event, taskList.size());
                        break;

                    case "mark":
                        voyager.task.Task marked = taskList.mark(Integer.parseInt(args) - 1);
                        storage.save(taskList.getAll());
                        ui.showTaskMarked(marked);
                        break;

                    case "unmark":
                        voyager.task.Task unmarked = taskList.unmark(Integer.parseInt(args) - 1);
                        storage.save(taskList.getAll());
                        ui.showTaskUnmarked(unmarked);
                        break;

                    case "delete":
                        voyager.task.Task removed = taskList.remove(Integer.parseInt(args) - 1);
                        storage.save(taskList.getAll());
                        ui.showTaskRemoved(removed, taskList.size());
                        break;

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

                        ui.showFoundTasks(matchingTasks);
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

