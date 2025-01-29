import javafx.application.Application;

/**
 * Main class that serves as the entry point for the application.
 * It launches the FrontPage GUI and allows the user to start the game.
 */
public class Main {

    /**
     * Main method to start the JavaFX application.
     * It launches the FrontPage scene, which serves as the starting point of the game.
     *
     * @param args Command-line arguments passed when launching the application.
     */
    public static void main(String[] args) {
        // Launch the JavaFX application, using the FrontPage class to display the initial UI
        Application.launch(FrontPage.class, args);
    }

    /**
     * Method to start the game, called from the FrontPage class when the user clicks the "Start Game" button.
     * It sets up the game board with a predefined tile size and dimensions, then launches the GameWindow.
     *
     * @note This method is executed on a separate thread to update the UI.
     */
    public static void startGame() {
        // Define the number of rows and columns for the game grid
        int rowCount = 21;
        int columnCount = 19;

        // Define the size of each tile in the grid
        int tileSize = 32;

        // Calculate the total width and height of the game board
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        // Launch the GameWindow in a new thread (using Platform.runLater)
        javafx.application.Platform.runLater(() -> {
            // Create and start a new GameWindow instance (the actual game window)
            GameWindow gameWindow = new GameWindow();
            gameWindow.start(new javafx.stage.Stage());
        });
    }
}
