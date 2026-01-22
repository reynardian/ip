import java.util.Scanner;

public class Voyager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = "_".repeat(60);

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
                break; // Exit the loop
            }

            System.out.println(input); // Echo the command
        }

        scanner.close();
    }
}
