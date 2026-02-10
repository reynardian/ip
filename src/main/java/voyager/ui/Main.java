package voyager.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import voyager.Voyager;

import java.io.IOException;

/**
 * Application entry point for the JavaFX GUI.
 */
public class Main extends Application {
    private Voyager voyager = new Voyager();

    /**
     * Initializes the main stage and sets up the layout and event handlers.
     *
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setVoyager(voyager);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}