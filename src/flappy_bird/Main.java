package flappy_bird;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Main implements ActionListener, MouseListener, KeyListener {

    public static Main main;
    public static final int WIDTH = 1200, HEIGHT = 800;
    public Renderer renderer;
    public Random rand;
    public static ArrayList<Rectangle> columns;
    public static int ticks, yMotion, score;
    public static boolean gameOver = false, started = false;

    private static BufferedImage birdImage;
    private static BufferedImage columnImage;
    private static Image backgroundImage;
    private static Rectangle bird;

    public Main() throws IOException {
        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();
        yMotion = 0;
        score = 0;

        jFrame.add(renderer);
        jFrame.setTitle("Flappy bird");
        jFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        jFrame.pack();
        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);
        jFrame.setVisible(true);
        jFrame.setResizable(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        birdImage = ImageIO.read(new FileInputStream("bird.png"));
        columnImage = ImageIO.read(new FileInputStream("hand.jpg"));
        backgroundImage = new ImageIcon(new URL("https://data.whicdn.com/images/280301626/original.gif")).getImage();
        bird = new Rectangle(WIDTH/2-50,HEIGHT/2 - 40,100,70);
        columns = new ArrayList<Rectangle>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int speed = 10 + score/100;

        ticks++;
        if(started) {
            for (Rectangle column : columns) {
                column.x -= speed;
            }

            if (ticks % 2 == 0 && (yMotion < 15)) {
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.width + column.x < 0) {
                    columns.remove(column);
                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }

            bird.y += yMotion;

            for (Rectangle column : columns) {
                if(column.y == 0 && bird.x  + bird.width / 2 > column.x + column.width/2 - 10 && bird.x  + bird.width / 2 < column.x + column.width/2 + 10){
                    score += 100;
                }
                if (column.intersects(bird))
                {
                    gameOver = true;

                    if (bird.x <= column.x)
                    {
                        bird.x = column.x - bird.width;

                    }
                    else
                    {
                        if (column.y != 0)
                        {
                            bird.y = column.y - bird.height;
                        }
                        else if (bird.y < column.height)
                        {
                            bird.y = column.height;
                        }
                    }
                }
            }

            if (bird.y > HEIGHT - 120 - bird.height || bird.y < 0) {
                gameOver = true;
            }

            if(bird.y + yMotion >= HEIGHT - 120 - bird.height) {
                bird.y = HEIGHT - 120 - bird.height;
            }

        }
        renderer.repaint();
    }

    public void addColumn(boolean start) {
        int space = 400;
        int width = 100;
        int height = 50 + rand.nextInt(300);
        if(start) {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    public static void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.red.darker());
        g.fillRect(column.x,column.y,column.width,column.height);
    }

    public void jump() {
        if(gameOver) {
            bird = new Rectangle(WIDTH/2-50,HEIGHT/2 - 40,100,70);
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            gameOver = false;
        }

        if(!started) {
            started = true;
        } else if(!gameOver) {
            if(yMotion>0) {
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }

    public static void repaint(Graphics g) throws MalformedURLException {

//        g.setColor(Color.cyan);
//        g.fillRect(0,0,WIDTH,HEIGHT);
        g.drawImage(backgroundImage,0,0,WIDTH,HEIGHT, null);

        g.setColor(Color.gray);
        g.fillRect(0,HEIGHT-120,WIDTH,120);

        g.drawImage(birdImage,bird.x,bird.y,bird.width,bird.height,null);

        for (Rectangle column:columns) {
            paintColumn(g,column);
        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial",1,100));

        if(gameOver) {
            g.drawString("Game over!", 300, HEIGHT / 2 - 50);
        }

        if(!started) {
            g.drawString("Click to start!", 300, HEIGHT / 2 - 50);
        }

        if(!gameOver && started) {
            g.drawString(String.valueOf(score) + "€", WIDTH/2  - 25, 100);
        }
    }

    public static void main(String[] args) throws IOException {
        main = new Main();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            jump();
        }
    }
}
