package com.guinea;

import com.guinea.game.Direction;
import com.guinea.game.Drawable;
import com.guinea.game.GameColors;
import com.guinea.game.Grid;
import com.guinea.game.snake.Snake;
import com.guinea.game.snake.SnakeType;
import com.guinea.geomatry.MatrixUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindow extends DoubleBuffer implements KeyListener {

    private TimerTask snakeTask;
    public Timer gameloopTimer = new Timer("gameloop");
    public Timer bgTimer = new Timer("background");
    public int[][] gameBoardMatrix = new int[10][10];
    private final Random random = new Random();
    public Snake snake = new Snake(5, 5, SnakeType.NORMAL);
    private int appleX = 0;
    private int appleY = 0;

    private static final Object moveLock = new Object();
    private boolean isDead = false;
    private final int snakeMoveDelay = 500;
    private Direction moveIn = Direction.RIGHT;
    private final BufferedImage normalHead = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\snake\\flushed.png"));
    private final BufferedImage fatHead = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\snake\\fat.png"));
    private final BufferedImage evilHead = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\snake\\evil.png"));
    private final BufferedImage madHead = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\snake\\mad.png"));
    private final BufferedImage apple = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\apple.png"));
    private boolean isMoveCanceled = false;
    Object msgJson = new JSONParser().parse(new FileReader("D:\\java-projects\\Snake\\src\\main\\resources\\deathmsg.json"));
    private String deathMessage;

    MainWindow() throws IOException, ParseException {
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

        snakeTask = new TimerTask() {
            @Override
            public void run() {
                updateSnake();
            }
        };

        TimerTask abilityTask = new TimerTask() {
            @Override
            public void run() {
                int num = random.nextInt(10);
                if (num == 2 && MatrixUtils.distance(snake.getSection(0).getX(),snake.getSection(0).getY(),appleX,appleY) > 2) snake.setType(SnakeType.FAT);
                if (num == 7 && snake.getSection(0).getX() > 0 && snake.getSection(0).getY() > 0 && snake.getSection(0).getX() < 9 && snake.getSection(0).getY() < 9) snake.setType(SnakeType.EVIL);
                else snake.setType(SnakeType.NORMAL);
            }
        };

        generateApple();

        //schedule gameloop tasks
        gameloopTimer.schedule(snakeTask, 0, snakeMoveDelay);
        bgTimer.schedule(abilityTask, 0, snakeMoveDelay);

        //init matrix
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gameBoardMatrix[i][j] = 0;
            }
        }
    }


    //Main logic happens here
    @Override
    public void paintBuffer(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 600, 600);

        //draw grid
        Grid grid = new Grid(10, 35, 10, 10, 50, g);

        Drawable plainSquare = (scale, g1, x, y) -> {
            g1.setColor(Color.BLACK);
            g1.fillRect(x, y, scale, scale);
            g1.setColor(GameColors.bgGray);
            g1.fillRoundRect(x, y, scale, scale, 10, 10);
        };
        Drawable appleSquare = (scale, g1, x, y) -> {
            g1.setColor(Color.BLACK);
            g1.fillRect(x, y, scale, scale);
            g1.setColor(GameColors.bgGray);
            g1.fillRoundRect(x, y, scale, scale, 10, 10);
            g1.drawImage(apple, x, y, this);
        };
        Drawable snakeSquare = (scale, g1, x, y) -> {
            g1.setColor(Color.BLACK);
            g1.fillRect(x, y, scale, scale);
            g1.setColor(GameColors.bgGray);
            g1.fillRoundRect(x, y, scale, scale, 10, 10);
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
            g1.fillRoundRect(x, y, scale, scale, 10, 10);
        };
        Drawable snakeHeadSquare = (scale, g1, x, y) -> {
            g1.setColor(Color.BLACK);
            g1.fillRect(x, y, scale, scale);
            g1.setColor(GameColors.bgGray);
            g1.fillRoundRect(x, y, scale, scale, 10, 10);
            switch (snake.getType()) {
                case NORMAL:
                    g1.drawImage(normalHead, x, y, this);
                    break;
                case FAT:
                    g1.drawImage(fatHead, x, y, this);
                    break;
                case EVIL:
                    g1.drawImage(evilHead, x, y, this);
                    break;
                case RAM:
                    g1.drawImage(madHead, x, y, this);
            }
        };

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (gameBoardMatrix[i][j] == 1) {
                    grid.drawAt(i, j, appleSquare);
                } else if (gameBoardMatrix[i][j] == 2) {
                    grid.drawAt(i, j, snakeSquare);
                } else if (gameBoardMatrix[i][j] == 3) {
                    grid.drawAt(i, j, snakeHeadSquare);
                } else grid.drawAt(i, j, plainSquare);
            }
        }

        if (isDead) {
            gameloopTimer.cancel();

            JSONObject jo = (JSONObject) msgJson;
            JSONArray normalMessages = (JSONArray) jo.get("normal_messages");
            JSONArray fatMessages = (JSONArray) jo.get("fat_messages");
            JSONArray evilMessages = (JSONArray) jo.get("evil_messages");

            switch (snake.getType()) {
                case FAT:
                    int rand = random.nextInt(fatMessages.size());
                    deathMessage = (String) fatMessages.get(rand);
                    break;
                case EVIL:
                    int rand1 = random.nextInt(evilMessages.size());
                    deathMessage = (String) evilMessages.get(rand1);
                    break;
                case NORMAL:
                    int rand2 = random.nextInt(normalMessages.size());
                    deathMessage = (String) normalMessages.get(rand2);
                    break;
            }

            g.setColor(new Color(36, 33, 32, 127));
            g.fillRect(0, 0, 600, 600);
            g.setColor(Color.WHITE);
            g.setFont(new Font("", Font.BOLD, 40));
            g.drawString("GAME OVER", 140, 275);
            g.setFont(new Font("", Font.BOLD, 20));
            g.drawString("SCORE: " + (snake.getLength() - 1), 8, 50);
            g.drawString("PRESS ENTER TO RESTART", 235, 50);
            g.drawString(deathMessage, 140,300);
        }
    }

    public void updateSnake() {

        synchronized (moveLock) {
            if (isMoveCanceled) {
                isMoveCanceled = false;
                return;
            }
            try {
                boolean removeTail = true;

                //check if player ate apple
                if ((snake.getSection(0).getX() == appleX) && (snake.getSection(0).getY() == appleY)) {
                    if (snake.getType().equals(SnakeType.FAT)) die("FAT_FAILURE");
                    else if (snake.getType().equals(SnakeType.RAM)) snake.setType(SnakeType.NORMAL);
                    removeTail = false;
                    generateApple();
                }

                //ram snake
                if (((snake.getSection(0).getX() >= 7) || (snake.getSection(0).getX() <= 2)) ||
                        ((snake.getSection(0).getY() >= 7) || (snake.getSection(0).getY() <= 2)) && snake.getType().equals(SnakeType.RAM))
                            snake.setType(SnakeType.NORMAL);


                //die
                for (int i = 0; i < snake.getLength() - 1; i++)
                    if (snake.getSection(i).isHead())
                        if (gameBoardMatrix[snake.getSection(i).getX()][snake.getSection(i).getY()] == 2)
                            die("yourself");

                //reset board
                resetMatrix();
                gameBoardMatrix[appleX][appleY] = 1;

                //move snake
                snake.addSection(moveIn, removeTail);

                //update game board
                for (int i = 0; i < snake.getLength() - 1; i++)
                    if (snake.getSection(i).isHead()) {
                        try {
                            gameBoardMatrix[snake.getSection(i).getX()][snake.getSection(i).getY()] = 3;
                        } catch (IndexOutOfBoundsException e) {
                            die("WALL AGAIN!!");
                        }
                    } else gameBoardMatrix[snake.getSection(i).getX()][snake.getSection(i).getY()] = 2;

            } finally {
                repaint();
            }
        }
    }

    void generateApple() {
        appleX = random.nextInt(10);
        appleY = random.nextInt(10);
        for (int i = 0; i < snake.getLength() - 1; i++)
            if ((appleX == snake.getSection(i).getX()) && (appleY == snake.getSection(i).getY())) generateApple();
        gameBoardMatrix[appleX][appleY] = 1;
    }

    void resetMatrix() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gameBoardMatrix[i][j] = 0;
            }
        }
    }

    void die(String reason) throws DiedException {
        isDead = true;
        throw new DiedException(reason);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        synchronized (moveLock) {
            if (snake.getType().equals(SnakeType.EVIL))
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (!moveIn.equals(Direction.UP))
                            changeMove(Direction.DOWN);
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!moveIn.equals(Direction.DOWN))
                            changeMove(Direction.UP);
                        break;
                    case KeyEvent.VK_LEFT:
                        if (!moveIn.equals(Direction.LEFT))
                            changeMove(Direction.RIGHT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!moveIn.equals(Direction.RIGHT))
                            changeMove(Direction.LEFT);
                }
            else if (!snake.getType().equals(SnakeType.RAM))
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (!moveIn.equals(Direction.DOWN))
                            changeMove(Direction.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!moveIn.equals(Direction.UP))
                            changeMove(Direction.DOWN);
                        break;
                    case KeyEvent.VK_LEFT:
                        if (!moveIn.equals(Direction.RIGHT))
                            changeMove(Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!moveIn.equals(Direction.LEFT))
                            changeMove(Direction.RIGHT);
                        break;
                }
            if (isDead && e.getKeyCode() == KeyEvent.VK_ENTER) {
                this.isDead = false;
                generateApple();
                this.gameloopTimer = new Timer("gameloop");
                this.snakeTask = new TimerTask() {
                    @Override
                    public void run() {
                        updateSnake();
                    }
                };
                this.gameloopTimer.schedule(snakeTask, 0,snakeMoveDelay);
                this.snake = new Snake(5, 5, SnakeType.NORMAL);
                this.moveIn = Direction.RIGHT;
            }
        }
    }

    private void changeMove(Direction newDirection) {
        moveIn = newDirection;
        isMoveCanceled = true;
        snakeTask.cancel();
        gameloopTimer.purge();
        snakeTask = new TimerTask() {
            @Override
            public void run() {
                updateSnake();
            }
        };
        gameloopTimer.schedule(snakeTask, 0,snakeMoveDelay);
        updateSnake();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public boolean isDoubleBuffered() {
        return true;
    }

}