import java.util.Scanner;

public class Voyager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = "_".repeat(60);

        String[] tasks = new String[100];
        int taskCount = 0;

        System.out.println(line);
        System.out.println("Hello! I'm Voyager");
        System.out.println("What can I do for you?");
        System.out.println(line);

        while (true) {
            String input = scanner.nextLine();
            System.out.println(line);

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(line);
                break; // exit the loop
            } else if (input.equalsIgnoreCase("list")) {
                if (taskCount == 0) {
                    System.out.println("No tasks added yet.");
                } else {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + ". " + tasks[i]);
                    }
                }
            } else {
                if (taskCount < 100) {
                    tasks[taskCount] = input;
                    taskCount++;
                    System.out.println("added: " + input);
                } else {
                    System.out.println("Task list is full!");
                }
            }
        }

        scanner.close();
    }
}
