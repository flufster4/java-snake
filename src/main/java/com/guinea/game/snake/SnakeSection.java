package com.guinea.game.snake;

import com.guinea.game.Direction;

public class SnakeSection {

    public boolean isHead = false;
    public boolean isTail = false;
    private final Direction facing;
    private final int x;
    private final int y;

    SnakeSection(boolean isHead, boolean isTail, Direction facing, int x, int y) {
        this.isHead = isHead;
        this.isTail = isTail;
        this.facing = facing;
        this.x = x;
        this.y =y;
    }

    public Direction getFacing() {
        return facing;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}