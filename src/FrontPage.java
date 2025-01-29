import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * FrontPage class serves as the introduction screen of the Pac-Man game.
 * It contains the title, the start button, and the exit button, along with
 * custom styling and animations for the user interface.
 */
public class FrontPage extends Application {

    /**
     * The start method is the main entry point for the JavaFX application.
     * It sets up the GUI with a title, buttons, and background image.
     *
     * @param primaryStage The primary stage for this JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Set up the primary stage (window) for the game
        primaryStage.setTitle("Pac-Man");

        // Create and style the title text for the game
        Text titleText = new Text("Pac-Man Game");
        titleText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-fill: yellow; -fx-font-family: 'Arial Black';");
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLUE);  // Set the shadow color
        dropShadow.setRadius(5);  // Set the shadow size
        titleText.setEffect(dropShadow);  // Apply the shadow effect to the title

        // Create the start and exit buttons with custom styles
        Button startButton = createStyledButton("Start Game");
        Button exitButton = createStyledButton("Exit");

        // Add an event handler to the start button
        startButton.setOnAction(e -> {
            try {
                // Launch the game window when the "Start Game" button is clicked
                GameWindow gameWindow = new GameWindow();
                gameWindow.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();  // Handle any exceptions that might occur
            } finally {
                primaryStage.close();  // Close the front page after launching the game
            }
        });

        // Add an event handler to the exit button to close the application
        exitButton.setOnAction(e -> primaryStage.close());

        // Arrange all elements in a vertical box layout
        VBox vbox = new VBox(40, titleText, startButton, exitButton);
        vbox.setAlignment(Pos.CENTER);  // Center the elements in the VBox

        // Set background image for the front page
        Image backgroundImage = new Image("file:C:/Users/alivi/OneDrive/Desktop/PacMan/src/resource/pac-man_facebook_cover_1.jpg");

        // Define the custom position and size for the background image
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(
                        BackgroundPosition.DEFAULT.getHorizontalSide(),
                        -0.25,  // Move the background 25% to the left
                        false,   // Proportional resizing for X axis (false means fixed width)
                        BackgroundPosition.DEFAULT.getVerticalSide(),
                        0,
                        true     // Proportional resizing for Y axis
                ),
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true) // Auto size the background
        );

        // Apply the background to the VBox container
        vbox.setBackground(new Background(background));

        // Set the scene with the VBox as the root node
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);  // Set the scene to the primary stage
        primaryStage.show();  // Display the primary stage
    }

    /**
     * Creates a styled button with custom design and hover/click animations.
     *
     * @param text The text to display on the button.
     * @return The styled button.
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-padding: 15px 30px; " +
                        "-fx-background-color: yellow; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: blue; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px;"
        );

        // Add hover effect to change the button background color
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: #FFFF66;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle() + "-fx-background-color: yellow;"));

        // Add click animation to the button (scale transition)
        button.setOnMousePressed(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(0.9);  // Reduce the button size on press
            st.setToY(0.9);
            st.play();
        });
        button.setOnMouseReleased(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1);  // Reset the button size after release
            st.setToY(1);
            st.play();
        });

        return button;
    }

    /**
     * Main entry point for launching the FrontPage application.
     *
     * @param args Command-line arguments passed when launching the application.
     */
    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}
