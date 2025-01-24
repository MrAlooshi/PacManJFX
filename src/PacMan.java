import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class PacMan extends Canvas {
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;
    class Block {
        private int x;
        private int y;
        private int width;
        private int height;
        private Image image;

        private int startX;
        private int startY;
        private char direction = 'U'; // U D L R
        private int velocityX = 0;
        private int velocityY = 0;

        public Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

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

        void updateVelocity(boolean isPacman) {
            int speed;
            if (isPacman) {
                speed = tileSize / 4;  // Pac-Man speed
            } else {
                speed = tileSize / 10; // Ghost speed (slower)
            }

            // Update velocity based on direction
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -speed;
            } else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = speed;
            } else if (this.direction == 'L') {
                this.velocityX = -speed;
                this.velocityY = 0;
            } else if (this.direction == 'R') {
                this.velocityX = speed;
                this.velocityY = 0;
            }
        }



        public void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

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
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

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

    private void setRandomDirection(Block ghost) {
        char randomDirection = directions[random.nextInt(directions.length)];
        ghost.setDirection(randomDirection);
        ghost.updateVelocity(false);
    }

    private void loadMap() {
        for (int r = 0; r < tileMap.length; r++) {
            String row = tileMap[r];
            for (int c = 0; c < row.length(); c++) {
                char tile = row.charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                switch (tile) {
                    case 'X':
                        walls.add(new Block(new Image("wall.png"), x, y, tileSize, tileSize));
                        break;
                    case 'P':
                        pacman = new Block(new Image("pacmanRight.png"), x, y, tileSize, tileSize);
                        break;
                    case 'b':
                        Block blueGhost = new Block(new Image("blueGhost.png"), x, y, tileSize, tileSize);
                        setRandomDirection(blueGhost); // Set random initial direction for ghost
                        ghosts.add(blueGhost);
                        break;
                    case 'o':
                        Block orangeGhost = new Block(new Image("orangeGhost.png"), x, y, tileSize, tileSize);
                        setRandomDirection(orangeGhost); // Set random initial direction for ghost
                        ghosts.add(orangeGhost);
                        break;
                    case 'p':
                        Block pinkGhost = new Block(new Image("pinkGhost.png"), x, y, tileSize, tileSize);
                        setRandomDirection(pinkGhost); // Set random initial direction for ghost
                        ghosts.add(pinkGhost);
                        break;
                    case 'r':
                        Block redGhost = new Block(new Image("redGhost.png"), x, y, tileSize, tileSize);
                        setRandomDirection(redGhost); // Set random initial direction for ghost
                        ghosts.add(redGhost);
                        break;
                    case ' ':
                        foods.add(new Block(null, x + tileSize / 2 - 2, y + tileSize / 2 - 2, 4, 4));
                        break;
                }
            }
        }
    }
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

    private void draw(GraphicsContext gc) {
        // Clear the canvas and set the background to black
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


    private void resetPositions() {
        pacman.setX(pacman.startX);
        pacman.setY(pacman.startY);
        pacman.setVelocityX(0);
        pacman.setVelocityY(0);

        for (Block ghost : ghosts) {
            ghost.updateVelocity(false);  // false indicates it's a ghost
            ghost.setX(ghost.getX() + ghost.getVelocityX());
            ghost.setY(ghost.getY() + ghost.getVelocityY());
        }
    }

    private void sortGhostsByX() {
        ArrayList<Block> ghostList = new ArrayList<>(ghosts);
        ghostList.sort((g1, g2) -> Integer.compare(g1.getX(), g2.getX()));
        ghosts.clear();
        ghosts.addAll(ghostList);
    }
    private void move() {
        // Move Pac-Man
        pacman.setX(pacman.getX() + pacman.getVelocityX());
        pacman.setY(pacman.getY() + pacman.getVelocityY());

        // Check Pac-Man boundary wrapping
        if (pacman.getX() < 0) {
            pacman.setX(boardWidth - pacman.getWidth());
        } else if (pacman.getX() >= boardWidth) {
            pacman.setX(0);
        }

        // Handle Pac-Man collisions with walls
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.setX(pacman.getX() - pacman.getVelocityX());
                pacman.setY(pacman.getY() - pacman.getVelocityY());
                break;
            }
        }

        // Move ghosts
        for (Block ghost : ghosts) {
            ghost.updateVelocity(false); // Update ghost velocity

            // Move ghosts horizontally and vertically
            ghost.setX(ghost.getX() + ghost.getVelocityX());
            ghost.setY(ghost.getY() + ghost.getVelocityY());

            // Teleport ghosts if out of bounds horizontally
            if (ghost.getX() < 0) {
                ghost.setX(boardWidth - ghost.getWidth()); // Teleport to the right side
            } else if (ghost.getX() >= boardWidth) {
                ghost.setX(0); // Teleport to the left side
            }

            // Handle the middle row fix (row 9)
            if (ghost.getY() == tileSize * 9) {
                // Only update the direction if the ghost was moving horizontally
                if (ghost.getDirection() == 'L' || ghost.getDirection() == 'R') {
                    ghost.updateDirection(random.nextBoolean() ? 'U' : 'D'); // Choose randomly up or down
                }
            }

            // Handle ghost collisions with walls
            for (Block wall : walls) {
                if (collision(ghost, wall)) {
                    // When colliding with a wall, reverse movement and change direction randomly
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

        // Check for food collision and reload map if all food is eaten
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




    private void handleKeyPress(KeyCode code) {
        if (code == KeyCode.UP) {
            pacman.updateDirection('U');
            pacman.updateVelocity(true);  // true means it's Pac-Man
            pacman.setImage(pacmanUpImage);
        } else if (code == KeyCode.DOWN) {
            pacman.updateDirection('D');
            pacman.updateVelocity(true);
            pacman.setImage(pacmanDownImage);
        } else if (code == KeyCode.LEFT) {
            pacman.updateDirection('L');
            pacman.updateVelocity(true);
            pacman.setImage(pacmanLeftImage);
        } else if (code == KeyCode.RIGHT) {
            pacman.updateDirection('R');
            pacman.updateVelocity(true);
            pacman.setImage(pacmanRightImage);
        }
    }



    private boolean collision(Block a, Block b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }
}
