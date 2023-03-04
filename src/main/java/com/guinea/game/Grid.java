package com.guinea.game;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Grid {

    private final int rows;
    private final int columns;
    private final int gridBoxLength;
    private final int Xoffset;
    private final int Yoffset;

    private final Graphics g;


    public Grid(int Xoffset, int Yoffset, int rows, int columns, int gridBoxLength, Graphics g) {
        this.rows = rows;
        this.columns = columns;
        this.gridBoxLength = gridBoxLength;
        this.g = g;
        this.Xoffset = Xoffset;
        this.Yoffset = Yoffset;
    }

    public void drawAt(int row, int column, @NotNull Drawable drawCode) {

        if (!((row <= rows) && (column <= columns))) throw new IndexOutOfBoundsException("row or column specified is greater then max number of rows or columns in grid");
        int x = row * gridBoxLength + Xoffset;
        int y = column * gridBoxLength + Yoffset;

        drawCode.draw(gridBoxLength, g, x, y);
    }

}
