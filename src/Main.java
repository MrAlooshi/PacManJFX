import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        // Launch the JavaFX application
        Application.launch(FrontPage.class, args);
    }

    // Method to start the game (called from FrontPage)
    public static void startGame() {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        // Launch the JavaFX-based GameWindow
        javafx.application.Platform.runLater(() -> {
            GameWindow gameWindow = new GameWindow();
            gameWindow.start(new javafx.stage.Stage());
        });
    }
}
