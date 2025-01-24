import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create an instance of PacMan
        PacMan pacmanGame = new PacMan();

        // Add PacMan to the scene
        StackPane root = new StackPane();
        root.getChildren().add(pacmanGame);

        // Set up the JavaFX scene
        Scene scene = new Scene(root, pacmanGame.getWidth(), pacmanGame.getHeight());
        primaryStage.setTitle("Pac-Man Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the GameWindow
    }
}
