package voyager.ui;

public class Parser {
    public static String getCommandWord(String input) {
        return input.trim().split(" ")[0].toLowerCase();
    }

    public static String getArguments(String input) {
        String[] parts = input.trim().split(" ", 2);
        return parts.length < 2 ? "" : parts[1];
    }
}
