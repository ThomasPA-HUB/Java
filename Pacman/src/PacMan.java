import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {
    
    class Block {

        int m_x ,m_y;
        int m_width, m_height;
        Image m_image;

        int m_startX;
        int m_startY;
        char m_direction = 'U';
        int m_velocityX = 0;
        int m_velocityY = 0;
        boolean m_inPortal = false;

        Block(Image image, int x, int y, int width, int height) {
            this.m_image = image;
            this.m_x = x;
            this.m_y = y;
            this.m_height = height;
            this.m_width = width;
            this.m_startX = x;
            this.m_startY = y;
        }

        void updateDirection(char Direction)
        {
            char prevDirection = this.m_direction;
            this.m_direction = Direction;
            updateVelocity();
            this.m_x += this.m_velocityX;
            this.m_y += this.m_velocityY;

            for(Block wall : walls)
            {
                if(collision(this, wall))
                {
                    this.m_x -= this.m_velocityX;
                    this.m_y -= this.m_velocityY;
                    this.m_direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity()
        {
            switch (this.m_direction) 
            {
                case 'U' -> {
                    this.m_velocityX = 0;
                    this.m_velocityY = -tileSize / 4;
                }

                case 'D' -> {
                    this.m_velocityX = 0;
                    this.m_velocityY = tileSize / 4;
                }

                case 'L' -> {
                    this.m_velocityX = -tileSize / 4;
                    this.m_velocityY = 0;
                }

                case 'R' -> {
                    this.m_velocityX = tileSize / 4;
                    this.m_velocityY = 0;
                }

                default -> {
                    this.m_velocityX = 0;
                    this.m_velocityY = 0;
                }
            }
        }

        void reset()
        {
            this.m_x = this.m_startX;
            this.m_y = this.m_startY;
        }

    }

    private final int rowCount = 21;
    private final int columnCount = 19;
    private final int tileSize = 32;
    private final int boardWidth = columnCount * tileSize;
    private final int boardHeight = rowCount * tileSize;

    private final Image wallImage;

    private final Image blueGhostImage;
    private final Image orangeGhostImage;
    private final Image pinkGhostImage;
    private final Image redGhostImage;

    
    private final Image pacmanUpImage;
    private final Image pacmanDownImage;
    private final Image pacmanRightImage;
    private final Image pacmanLeftImage;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    HashSet<Block> portals;

    Block pacman;

    Timer gameloop;
    char[] directions = {'U', 'D', 'R', 'L'};
    Random random = new Random();

    int m_score =0;
    int m_lives = 3;
    boolean gameOver = false;

    //X = wall, O = skip, ' ', food ...
    private final String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "XXXX X         XXXX",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX   XXXX",
        "0                 0",
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

        addKeyListener(this);
        setFocusable(true);

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
        for(Block ghost : ghosts)
        {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }

        gameloop = new Timer(50, this);
        gameloop.start();

    }
    
    public void LoadMap()
    {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();
        portals = new HashSet<>();

        for(int r = 0; r < rowCount; r++) {
            for(int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c); //we have a char at the specific tile

                int x = c * tileSize;
                int y = r * tileSize;
                switch (tileMapChar)
                {
                    case 'X' -> {
                        // block wall
                        Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                        walls.add(wall);
                    }

                    case 'b' -> {
                        // blue ghost
                        Block blueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(blueGhost);
                    }

                    case 'o' -> {
                        // orange ghost
                        Block orangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(orangeGhost);
                    }

                    case 'r' -> {
                        // red ghost
                        Block redGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(redGhost);
                    }

                    case 'p' -> {
                        // pink ghost
                        Block pinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(pinkGhost);
                    }

                    case 'P' -> // pacman
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);

                    case 'O' -> {
                        Block portal = new Block(null, x, y, tileSize, tileSize);
                        portals.add(portal);
}
                    case ' ' -> {
                        // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                    }
                // caractère non reconnu
                    default -> {
                    }
                }
            }
        }
    }

    private void checkPortal(Block entity)
    {
        boolean touchingPortal = false;

        for (Block portal : portals)
        {
            if (collision(entity, portal))
            {
                touchingPortal = true;

                // trouver l'autre portail

                if (entity.m_inPortal) return;

                for (Block other : portals)
                {
                    if (other != portal)
                    {
                        entity.m_x = other.m_x + entity.m_velocityX * 2;
                        entity.m_y = other.m_y + entity.m_velocityY * 2;
                        entity.m_inPortal = true;
                        return; // empêche double téléport
                    }
                }
            }
        }
        if (!touchingPortal)
        {
            entity.m_inPortal = false;
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
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

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if(gameOver)
        {
            g.drawString("Game Over : " + String.valueOf(m_score), tileSize/2, tileSize/2);
        }
        else
        {
            g.drawString("x" + String.valueOf(m_lives) + " Score: " + String.valueOf(m_score), tileSize/2, tileSize/2);
        }
    }

    public void move()
    {
        pacman.m_x += pacman.m_velocityX; 
        pacman.m_y += pacman.m_velocityY;

        for(Block wall : walls)
        {
            if(collision(pacman, wall))
            {
                pacman.m_x -= pacman.m_velocityX;
                pacman.m_y -= pacman.m_velocityY;
                break;
            }
        }

        for(Block ghost : ghosts)
        {
            if(collision(ghost, pacman))
            {
                m_lives -=1;
                if(m_lives == 0){
                    gameOver = true;
                    return;
                }
                resetPosition();
            }
            if(ghost.m_y == tileSize*9 && ghost.m_direction != 'U' && ghost.m_direction != 'D')
                ghost.updateDirection('U');
            
            ghost.m_x += ghost.m_velocityX;
            ghost.m_y += ghost.m_velocityY;

            checkPortal(ghost);

            for(Block wall : walls)
            {
                if(collision(ghost, wall))
                {
                    ghost.m_x -= ghost.m_velocityX;
                    ghost.m_y -= ghost.m_velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }

        }

        //check food collision
        Block foodEaten = null;
        for(Block food : foods)
        {
            if(collision(pacman, food))
            {
                foodEaten = food;
                m_score += 10;
            }
        }
        foods.remove(foodEaten);
    }

    private boolean collision(Block a, Block b)
    {
        return 
        a.m_x < b.m_x + b.m_width  &&
        a.m_x + a.m_width > b.m_x  &&
        a.m_y < b.m_y + b.m_height &&
        a.m_y + a.m_height > b.m_y;
    }

    public void resetPosition(){
        pacman.reset();
        pacman.m_velocityX = 0;
        pacman.m_velocityY = 0;
        for(Block ghost : ghosts)
        {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();
        if(gameOver)
        {
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {

        if(gameOver)
        {
           LoadMap();
           resetPosition();
           m_lives = 3; 
           m_score = 0; 
           gameOver = false;
           gameloop.start();
        }

        switch(e.getKeyCode())
        {
            case KeyEvent.VK_UP -> {
                pacman.updateDirection('U');
            }
            case KeyEvent.VK_DOWN -> {
                pacman.updateDirection('D');
            }
            case KeyEvent.VK_RIGHT -> {
                pacman.updateDirection('R');
            }
            case KeyEvent.VK_LEFT -> {
                pacman.updateDirection('L');
            }
        }

        switch(pacman.m_direction)
        {
            case 'U' -> {
                pacman.m_image = pacmanUpImage;
            }
            case 'D' -> {
                pacman.m_image = pacmanDownImage;
            }
            case 'R' -> {
                pacman.m_image = pacmanRightImage;
            }
            case 'L' -> {
                pacman.m_image = pacmanLeftImage;
            }
            default -> {
                pacman.m_image = pacmanUpImage;
            }
        }

    }
        
}
