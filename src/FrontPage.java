import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FrontPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Set up the primary stage
        primaryStage.setTitle("Pac-Man");

        // Create a title label
        Text titleText = new Text("Pac-Man Game");
        titleText.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        // Create buttons
        Button startButton = new Button("Start Game");
        Button exitButton = new Button("Exit");

        // Style buttons
        startButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        exitButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");

        startButton.setOnAction(e -> {
            try {
                // Launch the GameWindow as a new JavaFX application
                GameWindow gameWindow = new GameWindow();
                gameWindow.start(new Stage()); // Safely start GameWindow
            } catch (Exception ex) {
                ex.printStackTrace(); // Handle any exceptions and print the stack trace
            } finally {
                primaryStage.close(); // Close the front page
            }
        });


        exitButton.setOnAction(e -> {
            // Exit the application
            primaryStage.close();
        });

        // Arrange elements in a vertical box
        VBox vbox = new VBox(20, titleText, startButton, exitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: black; -fx-padding: 20px;");

        // Set the scene and display the stage
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
