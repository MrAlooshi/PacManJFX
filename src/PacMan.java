import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;
public class PacMan extends JPanel {
    class Block {
        int x;
        int y;
        int height;
        int width;
        Image image;

        int startX;
        int startY;

        Block(Image image, int x, int y, int height, int width) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.height = height;
            this.width = width;
            this.startX = x;
            this.startY = y;

        }
    }
   private int rowCount= 21;
    private int columnCount= 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private  int boardHeight = rowCount * tileSize;

    private Image wallImage;

    private Image blueGhostImage;
     private Image redGhostImage;
     private Image orangeGhostImage;
    private Image pinkGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
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
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;
    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

        //load images
        wallImage = new ImageIcon(getClass().getResource("wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("blueGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("redGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("orangeGhost.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("pacmanRight.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("pinkGhost.png")).getImage();

    }
    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;
            }
        }
    }
}






// I'm going to store all the ghosts and pacman in three different hashsets, since theres only one pacman, i dont need to store it in a hashset, the reason why i choose a hashset, when i check for collision its easier to check in hashsets instead of an arraylist, so its only for performance, an hashset is similair as arraylist, only hashsets are better for looking out values, and all the values in a hashset is unique
// The tile map, 2d array, i will be using characters, you could use numbers, but i will use characters, because its easier to write out, so i will be using an array of strings, an array of strings is basically an array of an array of characters, each row is a string within our array, each tile is going to be represented by a character and if the character is an x i will be putting wall there, if there is empty tiles /white space, i will put the food there, the o means it is empty, its much easier to make the os empty instead of having to put o's at every white space instead, r is redghost and so on. P is Pacman i will iterate through our tilemap to create objects i will start at 00, thats the top left corner, If you see the x axis, i will start there, and go further meaning (4,2) would be 4 x and 2 y, pixel wise we need to multiply each  by the tile size which is 32 px, the width and height of the tile is 32 px,