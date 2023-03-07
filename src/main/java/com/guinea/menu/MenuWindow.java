package com.guinea.menu;

import com.guinea.MainWindow;
import com.guinea.game.Drawable;
import com.guinea.game.GameColors;
import com.guinea.game.Grid;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MenuWindow extends JFrame {

    private final BufferedImage apple = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\apple.png"));

    Timer loop = new Timer("loop");

    public MenuWindow() throws IOException {

        setTitle("Snake+");
        BufferedImage icon = ImageIO.read(new File("D:\\java-projects\\Snake\\src\\main\\resources\\snake\\flushed.png"));
        setIconImage(icon);
        setSize(500,500);
        setResizable(false);
        setLayout(null);

        MenuButton button = new MenuButton("test");
        button.setBounds(195,225, 100,50);
        button.setText("Start");

        add(button);
        repaint();
        setVisible(true);

        TimerTask buttonPressed = new TimerTask() {
            @Override
            public void run() {
                if (button.getModel().isPressed()) {
                    setVisible(false);
                    loop.cancel();
                    MainWindow mainWindow = null;
                    try {
                        mainWindow = new MainWindow();
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                    assert mainWindow != null;
                    mainWindow.repaint();
                }
            }
        };

        loop.schedule(buttonPressed,0,100);
    }

    @Override
    public void paint(Graphics g) {

        Grid grid = new Grid(0,0,10,10,50, g);

        g.setColor(Color.BLACK);
        g.fillRect(0,0,500,500);

        Drawable plainSquare = (scale, g1, x, y) -> {
            g.setColor(GameColors.bgGray);
            g.fillRoundRect(x,y,scale,scale,10,10);
        };
        Drawable snakeSquare = (scale, g1, x, y) -> {
            g.setColor(GameColors.snakeYellow);
            g.fillRoundRect(x,y,scale,scale,10,10);
        };
        Drawable greenSnakeSquare = (scale, g1, x, y) -> {
            g.setColor(GameColors.snakeGreen);
            g.fillRoundRect(x,y,scale,scale,10,10);
        };
        Drawable appleSquare = (scale, g1, x, y) -> {
            g.drawImage(apple,x,y,this);
        };

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid.drawAt(i,j,plainSquare);
            }
        }

        grid.drawAt(5,0,snakeSquare);
        grid.drawAt(5,1,snakeSquare);
        grid.drawAt(5,2,snakeSquare);
        grid.drawAt(4,2,snakeSquare);
        grid.drawAt(3,2,snakeSquare);
        grid.drawAt(2,2,snakeSquare);
        grid.drawAt(1,2,snakeSquare);
        grid.drawAt(0,2,snakeSquare);

        grid.drawAt(7,9,greenSnakeSquare);
        grid.drawAt(7,8,greenSnakeSquare);
        grid.drawAt(7,7,greenSnakeSquare);
        grid.drawAt(8,7,greenSnakeSquare);
        grid.drawAt(9,7,greenSnakeSquare);

        grid.drawAt(6,5, appleSquare);

    }
}
