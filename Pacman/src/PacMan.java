import java.awt.*;
import java.awt.event.*;
import java.awt.font.GraphicAttribute;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel{
    
    class Block {

        int m_x ,m_y;
        int m_width, m_height;
        Image m_image;

        int m_startX;
        int m_startY;

        Block(Image image, int x, int y, int width, int height) {
            this.m_image = image;
            this.m_x = x;
            this.m_y = y;
            this.m_height = height;
            this.m_width = width;
            this.m_startX = x;
            this.m_startY = y;
        }

    }


    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;

    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanRightImage;
    private Image pacmanLeftImage;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    

    //X = wall, O = skip, ' ', food ...
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


    public PacMan( ) 
    {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

        // load Images
        wallImage = new ImageIcon(getClass().getResource("./img/wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./img/blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./img/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./img/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./img/redGhost.png")).getImage();
        
        pacmanUpImage = new ImageIcon(getClass().getResource("./img/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./img/pacmanDown.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("img/pacmanRight.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./img/pacmanLeft.png")).getImage();

        LoadMap();
        
    }
    
    public void LoadMap()
    {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for(int r = 0; r < rowCount; r++) {
            for(int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c); //we have a char at the specific tile

                int x = c * tileSize;
                int y = r * tileSize;
                switch (tileMapChar)
                {
                    case 'X': // block wall
                        Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                        walls.add(wall);
                        break;

                    case 'b': // blue ghost
                        Block blueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(blueGhost);
                        break;

                    case 'o': // orange ghost
                        Block orangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(orangeGhost);
                        break;

                    case 'r': // red ghost
                        Block redGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(redGhost);
                        break;

                    case 'p': // pink ghost
                        Block pinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(pinkGhost);
                        break;

                    case 'P': // pacman
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;

                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;

                    default:
                        // caractère non reconnu
                        break;
                }
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        System.out.println("Pacman X=" + pacman.m_x + " Y=" + pacman.m_y);
        g.drawImage(pacman.m_image, pacman.m_x, pacman.m_y, pacman.m_width, pacman.m_height, null);

        for(Block ghost : ghosts)
        {
            g.drawImage(ghost.m_image, ghost.m_x, ghost.m_y, ghost.m_width, ghost.m_height, null);
        }

        for(Block wall : walls)
        {
            g.drawImage(wall.m_image, wall.m_x, wall.m_y, wall.m_width, wall.m_height, null);
        }
        
        g.setColor(Color.WHITE);
        for(Block food : foods)
        {
            g.fillRect(food.m_x, food.m_y, food.m_width, food.m_height);
        }
    }
        
}
