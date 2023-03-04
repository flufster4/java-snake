package com.guinea.game;

import java.awt.*;

public interface Drawable {

    /**
     *  The code for drawing
     * @param g the graphics class instance
     * @param scale the scale that it should be drawn at
     * @param x the X axis at which to draw
     * @param y the Y axis at which to draw
     */
    void draw(int scale, Graphics g, int x, int y);

}
