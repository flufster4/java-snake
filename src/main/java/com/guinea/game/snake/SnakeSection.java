package com.guinea.game.snake;

import com.guinea.game.Direction;

public class SnakeSection {

    private boolean isHead = false;
    private boolean isTail = false;
    private Direction facing;
    private final int x;
    private final int y;

    SnakeSection(boolean isHead, boolean isTail, Direction facing, int x, int y) {
        this.isHead = isHead;
        this.isTail = isTail;
        this.facing = facing;
        this.x = x;
        this.y =y;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean isHead) {
        this.isHead = isHead;
    }

    public boolean isTail() {
        return isTail;
    }

    public void setTail(boolean isTail) {
        this.isTail = isTail;
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