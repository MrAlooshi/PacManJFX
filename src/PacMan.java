import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * PacMan class represents the main gameplay logic.
 * It extends Canvas to display the game and handles the movement of Pac-Man, ghosts, food, and walls.
 */
public class PacMan extends Canvas {
    // Declare images for the different Pac-Man directions
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    // Block class represents objects in the game like Pac-Man, walls, ghosts, and food
    class Block {
        private int x, y, width, height;
        private Image image;
        private int startX, startY;
        private char direction = 'U'; // U D L R
        private int velocityX = 0, velocityY = 0;

        /**
         * Block constructor initializes an image, position, and size.
         *
         * @param image  The image representing the object (e.g., Pac-Man, ghosts, walls)
         * @param x      The x-coordinate of the block
         * @param y      The y-coordinate of the block
         * @param width  The width of the block
         * @param height The height of the block
         */
        public Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        // Getters and setters for block's attributes
        public int getX() { return x; }
        public void setX(int x) { this.x = x; }
        public int getY() { return y; }
        public void setY(int y) { this.y = y; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
        public Image getImage() { return image; }
        public void setImage(Image image) { this.image = image; }
        public char getDirection() { return direction; }
        public void setDirection(char direction) { this.direction = direction; }
        public int getVelocityX() { return velocityX; }
        public void setVelocityX(int velocityX) { this.velocityX = velocityX; }
        public int getVelocityY() { return velocityY; }
        public void setVelocityY(int velocityY) { this.velocityY = velocityY; }

        /**
         * Update the direction of the block and its velocity.
         * If Pac-Man, it moves based on user input; if ghosts, it moves based on AI logic.
         */
        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity(true);
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity(true);
                }
            }
        }

        /**
         * Update the velocity based on the direction.
         *
         * @param isPacman If true, it applies Pac-Man's speed; otherwise, ghost's speed.
         */
        void updateVelocity(boolean isPacman) {
            int speed = (isPacman) ? tileSize / 4 : tileSize / 10;
            if (this.direction == 'U') { this.velocityX = 0; this.velocityY = -speed; }
            else if (this.direction == 'D') { this.velocityX = 0; this.velocityY = speed; }
            else if (this.direction == 'L') { this.velocityX = -speed; this.velocityY = 0; }
            else if (this.direction == 'R') { this.velocityX = speed; this.velocityY = 0; }
        }

        /**
         * Resets the block to its initial position.
         */
        public void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    // Gameboard dimensions and setup
    private int rowCount = 21, columnCount = 19, tileSize = 32;
    private int boardWidth = columnCount * tileSize, boardHeight = rowCount * tileSize;

    // Tile map representing the game layout
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    private HashSet<Block> walls;
    private ArrayList<Block> foods;
    private HashSet<Block> ghosts;
    private Block pacman;

    private AnimationTimer gameLoop;
    private char[] directions = {'U', 'D', 'L', 'R'};
    private Random random = new Random();
    private int score = 0, lives = 3;
    private boolean gameOver = false;

    /**
     * Constructor for the PacMan class, sets up the game, loads images,
     * and initializes the game loop.
     */
    public PacMan() {
        setWidth(boardWidth);
        setHeight(boardHeight);
        walls = new HashSet<>();
        foods = new ArrayList<>();
        ghosts = new HashSet<>();

        pacmanUpImage = new Image("file:///C:/Users/alivi/OneDrive/Desktop/PacMan/src/resource/pacmanUp.png");
        pacmanDownImage = new Image("file:///C:/Users/alivi/OneDrive/Desktop/PacMan/src/resource/pacmanDown.png");
        pacmanLeftImage = new Image("file:///C:/Users/alivi/OneDrive/Desktop/PacMan/src/resource/pacmanLeft.png");
        pacmanRightImage = new Image("file:///C:/Users/alivi/OneDrive/Desktop/PacMan/src/resource/pacmanRight.png");

        loadMap();
        startGameLoop();

        setFocusTraversable(true);
        setOnKeyPressed(e -> handleKeyPress(e.getCode()));
    }

    /**
     * Sets a random direction for the ghost to move.
     */
    private void setRandomDirection(Block ghost) {
        char randomDirection = directions[random.nextInt(directions.length)];
        ghost.setDirection(randomDirection);
        ghost.updateVelocity(false);
    }

    /**
     * Loads the map, populates walls, food, and ghosts based on the tileMap.
     */
    private void loadMap() {
        for (int r = 0; r < tileMap.length; r++) {
            String row = tileMap[r];
            for (int c = 0; c < row.length(); c++) {
                char tile = row.charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                switch (tile) {
                    case 'X': walls.add(new Block(new Image("wall.png"), x, y, tileSize, tileSize)); break;
                    case 'P': pacman = new Block(new Image("pacmanRight.png"), x, y, tileSize, tileSize); break;
                    case 'b': Block blueGhost = new Block(new Image("blueGhost.png"), x, y, tileSize, tileSize); setRandomDirection(blueGhost); ghosts.add(blueGhost); break;
                    case 'o': Block orangeGhost = new Block(new Image("orangeGhost.png"), x, y, tileSize, tileSize); setRandomDirection(orangeGhost); ghosts.add(orangeGhost); break;
                    case 'p': Block pinkGhost = new Block(new Image("pinkGhost.png"), x, y, tileSize, tileSize); setRandomDirection(pinkGhost); ghosts.add(pinkGhost); break;
                    case 'r': Block redGhost = new Block(new Image("redGhost.png"), x, y, tileSize, tileSize); setRandomDirection(redGhost); ghosts.add(redGhost); break;
                    case ' ': foods.add(new Block(null, x + tileSize / 2 - 2, y + tileSize / 2 - 2, 4, 4)); break;
                }
            }
        }
    }

    /**
     * Starts the game loop using AnimationTimer to repeatedly update the game state.
     */
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                move();
                draw(getGraphicsContext2D());
            }
        };
        gameLoop.start();
    }

    /**
     * Draws the game state to the canvas, including Pac-Man, ghosts, walls, and food.
     */
    private void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, boardWidth, boardHeight); // Fill the background with black

        // Draw Pac-Man
        gc.drawImage(pacman.getImage(), pacman.getX(), pacman.getY(), pacman.getWidth(), pacman.getHeight());

        // Draw ghosts
        for (Block ghost : ghosts) {
            gc.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight());
        }

        // Draw walls
        for (Block wall : walls) {
            gc.drawImage(wall.getImage(), wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        }

        // Draw food
        gc.setFill(Color.WHITE);
        for (Block food : foods) {
            gc.fillRect(food.getX(), food.getY(), food.getWidth(), food.getHeight());
        }

        // Display score and lives
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 18));
        if (gameOver) {
            gc.fillText("Game Over: " + score, tileSize / 2, tileSize / 2);
        } else {
            gc.fillText("x" + lives + " Score: " + score, tileSize / 2, tileSize / 2);
        }
    }

    /**
     * Resets positions of Pac-Man and ghosts.
     */
    private void resetPositions() {
        pacman.setX(pacman.startX);
        pacman.setY(pacman.startY);
        pacman.setVelocityX(0);
        pacman.setVelocityY(0);

        for (Block ghost : ghosts) {
            ghost.updateVelocity(false);
            ghost.setX(ghost.getX() + ghost.getVelocityX());
            ghost.setY(ghost.getY() + ghost.getVelocityY());
        }
    }

    /**
     * Sorts ghosts by their X coordinate.
     */
    private void sortGhostsByX() {
        ArrayList<Block> ghostList = new ArrayList<>(ghosts);
        ghostList.sort((g1, g2) -> Integer.compare(g1.getX(), g2.getX()));
        ghosts.clear();
        ghosts.addAll(ghostList);
    }

    /**
     * Moves Pac-Man and ghosts based on their velocities, checks for boundary wrapping,
     * and handles collisions with walls, ghosts, and food.
     */
    private void move() {
        pacman.setX(pacman.getX() + pacman.getVelocityX());
        pacman.setY(pacman.getY() + pacman.getVelocityY());

        // Check boundary wrapping for Pac-Man
        if (pacman.getX() < 0) { pacman.setX(boardWidth - pacman.getWidth()); }
        else if (pacman.getX() >= boardWidth) { pacman.setX(0); }

        // Handle Pac-Man collisions with walls
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.setX(pacman.getX() - pacman.getVelocityX());
                pacman.setY(pacman.getY() - pacman.getVelocityY());
                break;
            }
        }

        // Move ghosts and handle their logic
        for (Block ghost : ghosts) {
            ghost.updateVelocity(false);
            ghost.setX(ghost.getX() + ghost.getVelocityX());
            ghost.setY(ghost.getY() + ghost.getVelocityY());

            if (ghost.getX() < 0) { ghost.setX(boardWidth - ghost.getWidth()); }
            else if (ghost.getX() >= boardWidth) { ghost.setX(0); }

            // Handle the middle row fix (row 9)
            if (ghost.getY() == tileSize * 9) {
                if (ghost.getDirection() == 'L' || ghost.getDirection() == 'R') {
                    ghost.updateDirection(random.nextBoolean() ? 'U' : 'D');
                }
            }

            // Handle ghost collisions with walls
            for (Block wall : walls) {
                if (collision(ghost, wall)) {
                    ghost.setX(ghost.getX() - ghost.getVelocityX());
                    ghost.setY(ghost.getY() - ghost.getVelocityY());
                    char newDirection = directions[random.nextInt(directions.length)];
                    ghost.updateDirection(newDirection);
                }
            }

            // Handle collision with Pac-Man
            if (collision(ghost, pacman)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }
        }

        // Check for food collision
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    /**
     * Handle user input for Pac-Man movement.
     */
    private void handleKeyPress(KeyCode code) {
        if (code == KeyCode.UP) { pacman.updateDirection('U'); pacman.updateVelocity(true); pacman.setImage(pacmanUpImage); }
        else if (code == KeyCode.DOWN) { pacman.updateDirection('D'); pacman.updateVelocity(true); pacman.setImage(pacmanDownImage); }
        else if (code == KeyCode.LEFT) { pacman.updateDirection('L'); pacman.updateVelocity(true); pacman.setImage(pacmanLeftImage); }
        else if (code == KeyCode.RIGHT) { pacman.updateDirection('R'); pacman.updateVelocity(true); pacman.setImage(pacmanRightImage); }
    }

    /**
     * Checks if two blocks have collided.
     *
     * @param a First block.
     * @param b Second block.
     * @return True if blocks are colliding, false otherwise.
     */
    private boolean collision(Block a, Block b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }
}
