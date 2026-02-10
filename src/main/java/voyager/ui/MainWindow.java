package voyager.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import voyager.Voyager;

/**
 * Controller for the main GUI window.
 * Provides the interface for the user to interact with Voyager, including
 * the scrollable dialog container, text input field, and send button.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Voyager voyager;

    private final Image USER_IMAGE = new Image(this.getClass().getResourceAsStream("/images/User.png"),
            50, 50, true, true);
    private final Image VOYAGER_IMAGE = new Image(this.getClass().getResourceAsStream("/images/Voyager.png"),
            50, 50, true, true);

    /**
     * Initializes the controller. Sets up the scroll pane to automatically
     * scroll to the bottom when new messages are added.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Voyager instance and displays the initial welcome message.
     * * @param v The Voyager logic instance to be used by the GUI.
     */
    public void setVoyager(Voyager v) {
        voyager = v;

        String welcomeMessage = voyager.getResponse("welcome_trigger");
        dialogContainer.getChildren().add(
                DialogBox.getVoyagerDialog(welcomeMessage, VOYAGER_IMAGE)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Voyager's reply.
     * Clears the user input after processing. If the input is "bye", the application
     * will exit after a short delay.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = voyager.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, USER_IMAGE),
                DialogBox.getVoyagerDialog(response, VOYAGER_IMAGE)
        );
        userInput.clear();

        if (input.equalsIgnoreCase("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}