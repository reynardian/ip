package voyager.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the loading and saving of tasks to a local text file.
 * This class ensures that task data persists across different sessions of the application.
 */
public class Storage {
    private static final String DATA_FOLDER = "data";
    private static final String DATA_FILE =
            DATA_FOLDER + File.separator + "voyager.txt";

    /**
     * Loads tasks from the data file into the provided task list.
     * If the data folder or file does not exist, they will be created.
     *
     * @param tasks The list where loaded tasks will be stored.
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
     * Saves all tasks currently in the list to the hard disk.
     *
     * @param tasks The list of tasks to be written to the file.
     * @throws IOException If there is an error writing to the data file.
     */
    public void save(List<voyager.task.Task> tasks) throws IOException {
        FileWriter writer = new FileWriter(DATA_FILE);

        for (voyager.task.Task task : tasks) {
            writer.write(encodeTask(task) + System.lineSeparator());
        }

        writer.close();
    }

    /**
     * Converts a Task object into a formatted string suitable for file storage.
     *
     * @param task The task to be encoded.
     * @return A pipe-separated string representing the task.
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