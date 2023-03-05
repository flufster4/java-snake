package com.guinea;

import com.guinea.game.*;
import com.guinea.game.snake.Snake;
import com.guinea.game.snake.SnakeType;
import com.guinea.geomatry.MatrixUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindow extends JFrame implements KeyListener {

    public Timer gameloopTimer = new Timer("gameloop");
    public int[][] gameBoardMatrix = new int[10][10];
    private final Random random = new Random();
    public Snake snake = new Snake(5,5, SnakeType.NORMAL);
    private int appleX = 0;
    private int appleY = 0;
    private boolean isDead = false;

    private int snakeMoveDelay = 500;
    private int snakeMoveDelaySnapshot = snakeMoveDelay;
    private Direction moveIn = Direction.RIGHT;
    private final BufferedImage normalHead = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\snake\\flushed.png"));
    private final BufferedImage fatHead = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\snake\\fat.png"));
    private final BufferedImage evilHead = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\snake\\evil.png"));
    private final BufferedImage madHead = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\snake\\mad.png"));
    private final BufferedImage apple = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\apple.png"));

    MainWindow() throws IOException {
        super("Snake+");
        setSize(520, 550);
        requestFocus();
        setFocusable(true);
        addKeyListener(this);
        setResizable(false);
        setVisible(true);
        setIconImage(normalHead);

        //Properly exit window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                 dispose();
                 System.exit(0);
            }
        });

        //this task is used to update the GUI 10 times per second
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        };
        TimerTask snakeTask = new TimerTask() {
            @Override
            public void run() {
                updateSnake();
            }
        };
        TimerTask snakeBgTask = new TimerTask() {
            @Override
            public void run() {
                int changeType = random.nextInt(20);
                if ((changeType == 11) && (MatrixUtils.distance(snake.getSection(0).getX(),snake.getSection(0).getY(),appleX,appleY) > 2)) snake.setType(SnakeType.FAT);
                else if (changeType == 4) snake.setType(SnakeType.EVIL);
                else if (changeType == 19 && !(((snake.getSection(0).getX() >= 7) || (snake.getSection(0).getX() <= 2)) ||
                        ((snake.getSection(0).getY() >= 7) || (snake.getSection(0).getY() <= 2)) && snake.getType().equals(SnakeType.RAM))) snake.setType(SnakeType.RAM);
                else if (!snake.getType().equals(SnakeType.RAM)){
                    snake.setType(SnakeType.NORMAL);
                    snakeMoveDelay = snakeMoveDelaySnapshot;
                }

                snakeMoveDelaySnapshot = snakeMoveDelay;
                if (snake.getType().equals(SnakeType.RAM)) snakeMoveDelay = 250;
                else snakeMoveDelay = snakeMoveDelay * 2;
            }
        };

        generateApple();
        //schedule gameloop tasks
        gameloopTimer.schedule(updateTask, 100,100);
        gameloopTimer.schedule(snakeTask, snakeMoveDelay, snakeMoveDelay);
        gameloopTimer.schedule(snakeBgTask, 1000,1000);

        //init matrix
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gameBoardMatrix[i][j] = 0;
            }
        }
    }


    //Main logic happens here
    @Override
    public void paint(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0,0,600,600);

        //draw grid
        Grid grid = new Grid(10,35,10,10,50, g);

        Drawable plainSquare = (scale, g1, x, y) -> {
            g1.setColor(Color.BLACK);
            g1.fillRect(x,y,scale,scale);
            g1.setColor(GameColors.bgGray);
            g1.fillRoundRect(x,y,scale,scale,10,10);
        };
        Drawable appleSquare = (scale, g1, x, y) -> {
            g1.setColor(Color.BLACK);
            g1.fillRect(x,y,scale,scale);
            g1.setColor(GameColors.bgGray);
            g1.fillRoundRect(x,y,scale,scale,10,10);
            g1.drawImage(apple, x, y,this);
        };
        Drawable snakeSquare = (scale, g1, x, y) -> {
            g1.setColor(Color.BLACK);
            g1.fillRect(x,y,scale,scale);
            g1.setColor(GameColors.bgGray);
            g1.fillRoundRect(x,y,scale,scale,10,10);
            switch (snake.getType()) {
                case NORMAL:
                    g1.setColor(GameColors.snakeYellow);
                    break;
                case EVIL:
                    g1.setColor(GameColors.snakePurple);
                    break;
                case FAT:
                    g1.setColor(GameColors.snakeGreen);
                    break;
                case RAM:
                    g1.setColor(GameColors.snakeRed);
            }
            g1.fillRoundRect(x,y,scale,scale,10,10);
        };
        Drawable snakeHeadSquare = (scale, g1, x, y) -> {
            g1.setColor(Color.BLACK);
            g1.fillRect(x,y,scale,scale);
            g1.setColor(GameColors.bgGray);
            g1.fillRoundRect(x,y,scale,scale,10,10);
            switch (snake.getType()) {
                case NORMAL:
                    g1.drawImage(normalHead,x,y,this);
                    break;
                case FAT:
                    g1.drawImage(fatHead,x,y,this);
                    break;
                case EVIL:
                    g1.drawImage(evilHead,x,y,this);
                    break;
                case RAM:
                    g1.drawImage(madHead, x, y, this);
            }
        };

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (gameBoardMatrix[i][j] == 1) {
                    grid.drawAt(i,j,appleSquare);
                } else if (gameBoardMatrix[i][j] == 2){
                    grid.drawAt(i,j,snakeSquare);
                } else if (gameBoardMatrix[i][j] == 3){
                    grid.drawAt(i,j,snakeHeadSquare);
                } else grid.drawAt(i, j, plainSquare);
            }
        }

        if(isDead) {
            gameloopTimer.cancel();
            g.setColor(new Color(36,33,32,127));
            g.fillRect(0,0,600,600);
            g.setColor(Color.WHITE);
            g.setFont(new Font("",Font.BOLD,40));
            g.drawString("GAME OVER", 140,275);
            g.setFont(new Font("", Font.BOLD,20));
            g.drawString("SCORE: "+(snake.getLength()-1),140,300);
        }
    }

    public void updateSnake() {

        boolean removeTail = true;

        //check if player ate apple
        if ((snake.getSection(0).getX() == appleX) && (snake.getSection(0).getY() == appleY)) {
            if (snake.getType().equals(SnakeType.FAT)) die();
            else if (snake.getType().equals(SnakeType.RAM)) snake.setType(SnakeType.NORMAL);
            removeTail = false;
            generateApple();
        }

        //ram snake
        if (((snake.getSection(0).getX() >= 7) || (snake.getSection(0).getX() <= 2)) ||
                ((snake.getSection(0).getY() >= 7) || (snake.getSection(0).getY() <= 2)) && snake.getType().equals(SnakeType.RAM)) {
            snake.setType(SnakeType.NORMAL);
        }

        //die
        try {
            Cordinates nextCords = snake.getNextCords(moveIn);
            if (gameBoardMatrix[nextCords.getX()][nextCords.getY()] == 2) {
                die();
                return;
            }
        } catch (IndexOutOfBoundsException e) {
            die();
            return;
        }

        //reset board
        resetMatrix();
        gameBoardMatrix[appleX][appleY] = 1;

        //move snake
        snake.addSection(moveIn, removeTail);

        //update game board
        for (int i = 0; i < snake.getLength()-1; i++) {
            if (snake.getSection(i).isHead()) {
                try {
                    gameBoardMatrix[snake.getSection(i).getX()][snake.getSection(i).getY()] = 3;
                } catch (IndexOutOfBoundsException e) {
                    die();
                    return;
                }
            } else {
                gameBoardMatrix[snake.getSection(i).getX()][snake.getSection(i).getY()] = 2;
            }
        }
    }

    void generateApple() {
        appleX = random.nextInt(10);
        appleY = random.nextInt(10);
        for (int i = 0; i < snake.getLength()-1; i++) if((appleX == snake.getSection(i).getX()) && (appleY == snake.getSection(i).getY())) generateApple();
        gameBoardMatrix[appleX][appleY] = 1;
    }

    void resetMatrix() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gameBoardMatrix[i][j] = 0;
            }
        }
    }

    void die() {
        System.out.println("you died!");
        isDead = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (snake.getType().equals(SnakeType.EVIL))
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (!moveIn.equals(Direction.UP)) moveIn = Direction.DOWN;
                    break;
                case KeyEvent.VK_DOWN:
                    if (!moveIn.equals(Direction.DOWN)) moveIn = Direction.UP;
                    break;
                case KeyEvent.VK_LEFT:
                    if (!moveIn.equals(Direction.LEFT)) moveIn = Direction.RIGHT;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!moveIn.equals(Direction.RIGHT)) moveIn = Direction.LEFT;
            }
        else if (!snake.getType().equals(SnakeType.RAM))
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (!moveIn.equals(Direction.DOWN)) moveIn = Direction.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    if (!moveIn.equals(Direction.UP)) moveIn = Direction.DOWN;
                    break;
                case KeyEvent.VK_LEFT:
                    if (!moveIn.equals(Direction.RIGHT)) moveIn = Direction.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!moveIn.equals(Direction.LEFT)) moveIn = Direction.RIGHT;
            }

    }

    @Override
    public void keyReleased(KeyEvent e) {}
}