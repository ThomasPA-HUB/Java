import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

public class ChromeDinosaur extends JPanel implements ActionListener, KeyListener {
    int m_boardWith = 750;
    int m_boardHeight = 250;

//image
    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;

    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;

    class Block {
        int m_x;
        int m_y;
        int m_width;
        int m_height;
        Image m_img;

        Block(int x, int y, int width, int height, Image img) {
            this.m_x = x;
            this.m_y = y;
            this.m_width = width;
            this.m_height = height;
            this.m_img = img;
        }
    }

    int dinosaurWidth = 88;
    int dinosaurHeight = 94;
    int dinosaurX = 50;
    int dinosaurY = m_boardHeight - dinosaurHeight;

    Block dinosaur;

    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;

    int cactusHeigth = 70;
    int cactusX = 700;
    int cactusY = m_boardHeight - cactusHeigth;
    ArrayList<Block> cactusArray;
    Timer gameloop;
    Timer placeCactusTimer;

    int score = 0;


    public ChromeDinosaur(Image dinosaurImg, Image dinosaurDeadImg, Image dinosaurJumpImg, Image cactus1Img, Image cactus2Img, Image cactus3Img, Block dinosaur, ArrayList<Block> cactusArray, Timer gameloop) {
        this.dinosaurImg = dinosaurImg;
        this.dinosaurDeadImg = dinosaurDeadImg;
        this.dinosaurJumpImg = dinosaurJumpImg;
        this.cactus1Img = cactus1Img;
        this.cactus2Img = cactus2Img;
        this.cactus3Img = cactus3Img;
        this.dinosaur = dinosaur;
        this.cactusArray = cactusArray;
        this.gameloop = gameloop;
    }

    //physics
    int velocityX = -12;

    int velocityY = 0;
    int gravity = 1;

    boolean gameOver = false;

    public ChromeDinosaur(){
        setPreferredSize(new Dimension(m_boardWith, m_boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);

        dinosaurImg = new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();

        cactus1Img = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();

        //dinosaurs
        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);

        //cactus array
        cactusArray = new ArrayList<Block>();

        //game timer
        gameloop = new Timer(1000/60, this);
        gameloop.start();

        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });
        placeCactusTimer.start();
    }

    void placeCactus() {

        if(gameOver)
            return;

        double placeCactusChance = Math.random(); // 0 - 0.99999
        if(placeCactusChance > .90) {
            Block cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeigth, cactus3Img);
            cactusArray.add(cactus);
        }
        else if (placeCactusChance > .70) {
            Block cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeigth, cactus2Img);
            cactusArray.add(cactus);
        }
        else if (placeCactusChance > .50){
            Block cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeigth, cactus1Img);
            cactusArray.add(cactus); 
        }

        if(cactusArray.size() > 10) {
            cactusArray.remove(0);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(dinosaur.m_img, dinosaur.m_x, dinosaur.m_y, dinosaur.m_width, dinosaur.m_height, null);
        
        for(Block cactus : cactusArray) {
            g.drawImage(cactus.m_img, cactus.m_x, cactus.m_y, cactus.m_width, cactus.m_height, null);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Courirer", Font.PLAIN, 32));
        if(gameOver)
            g.drawString("Game Over: "+String.valueOf(score), 10, 35);
        else
            g.drawString(String.valueOf(score), 10, 35);

    }

    public void move() {
        velocityY += gravity;
        dinosaur.m_y += velocityY;

        if(dinosaur.m_y > dinosaurY) {
            dinosaur.m_y = dinosaurY;
            velocityY = 0;
            dinosaur.m_img = dinosaurImg;
        }

        for(Block cactus : cactusArray) {
            cactus.m_x +=  velocityX;
            if(collision(dinosaur, cactus)) {
                gameOver = true;
                dinosaur.m_img = dinosaurDeadImg;
            }
       } 

       score++;
    }

    boolean collision(Block a, Block b) {
        return 
            a.m_x < b.m_x + b.m_width  &&
            a.m_x + a.m_width > b.m_x  &&
            a.m_y < b.m_y + b.m_height &&
            a.m_y + a.m_height > b.m_y;
    } 
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            placeCactusTimer.stop();
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(dinosaur.m_y == dinosaurY) {
                velocityY = -17;
                dinosaur.m_img = dinosaurJumpImg;
            }
        }

        if(gameOver) {
            dinosaur.m_y = dinosaurY;
            dinosaur.m_img = dinosaurImg;
            velocityY = 0;
            cactusArray.clear();
            score = 0;
            gameOver = false;
            gameloop.start();
            placeCactusTimer.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
