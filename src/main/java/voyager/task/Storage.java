package voyager.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Storage {
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

                voyager.task.Task task;

                switch (type) {
                    case "T":
                        task = new voyager.task.ToDo(desc);
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
    public void save(List<voyager.task.Task> tasks) throws IOException {
        FileWriter writer = new FileWriter(DATA_FILE);

        for (voyager.task.Task task : tasks) {
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
    private String encodeTask(voyager.task.Task task) {
        String status = task.isDone() ? "1" : "0";

        if (task instanceof voyager.task.ToDo) {
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
