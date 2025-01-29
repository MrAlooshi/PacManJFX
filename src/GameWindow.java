import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * GameWindow class is responsible for setting up and displaying the main game window.
 * It contains the logic to add the PacMan game instance to the scene and show it.
 */
public class GameWindow extends Application {

    /**
     * The start method is the main entry point for the GameWindow.
     * It sets up the game window by creating an instance of PacMan,
     * adding it to the scene, and displaying the stage.
     *
     * @param primaryStage The primary stage (window) for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Create an instance of the PacMan game
        PacMan pacmanGame = new PacMan();

        // Add the PacMan game instance to the scene using a StackPane
        StackPane root = new StackPane();
        root.getChildren().add(pacmanGame);  // Add the PacMan Canvas to the root layout

        // Set up the JavaFX scene with the root layout and PacMan dimensions
        Scene scene = new Scene(root, pacmanGame.getWidth(), pacmanGame.getHeight());

        // Set the title of the window and assign the scene
        primaryStage.setTitle("Pac-Man Game");
        primaryStage.setScene(scene);

        // Prevent window resizing
        primaryStage.setResizable(false);

        // Show the game window
        primaryStage.show();
    }

    /**
     * The main method serves as the entry point to launch the GameWindow application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);  // Launch the GameWindow application
    }
}
