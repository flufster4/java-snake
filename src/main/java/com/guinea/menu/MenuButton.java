package com.guinea.menu;

import javax.swing.*;
import java.awt.*;

public class MenuButton extends JButton {

    private String title;

    MenuButton(String title) {
        this.title = title;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.setFont(new Font("", Font.BOLD, 20));
        g.drawString(title,getBounds().x/2,getBounds().y/2);
        g.drawRoundRect(getBounds().x,getBounds().y,getBounds().width,getBounds().height,25,25);
    }
}
