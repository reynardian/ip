package voyager.ui;

/**
 * Deals with making sense of the user command.
 */
public class Parser {

    /**
     * Extracts the command word from the full input string.
     * * @param fullCommand The raw input from the user.
     * @return The first word of the command in lowercase.
     */
    public static String getCommandWord(String fullCommand) {
        // Standard: Use descriptive names like 'fullCommand' instead of 'input'
        return fullCommand.trim().split(" ")[0].toLowerCase();
    }

    /**
     * Extracts the arguments following the command word.
     * * @param fullCommand The raw input from the user.
     * @return The remaining string after the command word, or empty string if no arguments.
     */
    public static String getArguments(String fullCommand) {
        String[] words = fullCommand.trim().split(" ", 2);
        // Standard: Avoid ternary operators if they make the line too long/complex
        if (words.length < 2) {
            return "";
        }
        return words[1].trim();
    }
}