package voyager.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * A custom control using FXML to represent a dialog box in the chat interface.
 * Consists of an {@code ImageView} to represent the speaker's face and a
 * {@code Label} containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Private constructor for creating a DialogBox.
     * * @param text The message to be displayed in the box.
     * @param img The avatar image of the speaker.
     */
    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box so the ImageView is on the left and text on the right.
     * Used specifically for messages originating from the chatbot.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Factory method to create a dialog box for the user.
     * * @param text The user's input message.
     * @param img The user's avatar image.
     * @return A {@code DialogBox} containing the user's message.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Factory method to create a dialog box for Voyager.
     * The resulting box is flipped so the avatar appears on the left.
     * * @param text Voyager's response message.
     * @param img Voyager's avatar image.
     * @return A flipped {@code DialogBox} containing Voyager's message.
     */
    public static DialogBox getVoyagerDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }
}