package com.guinea.game.snake;

import com.guinea.game.Direction;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class Snake {

    protected int x;
    protected int y;
    protected SnakeType type;
    protected Deque<SnakeSection> snakeSections = new LinkedList<>();

    public Snake(int gridX, int gridY, SnakeType type) {
        this.x = gridX;
        this.y = gridY;
        this.type = type;
        snakeSections.addFirst(new SnakeSection(true, false, Direction.LEFT, x-1, y-1));
        snakeSections.addFirst(new SnakeSection(true, false, Direction.LEFT, x, y));
    }

    public void setType(SnakeType newType) {
        this.type = newType;
    }

    public SnakeType getType() {
        return type;
    }

    public void addSection(Direction moving, boolean removeTail) {
        //calculate new head x and y
        switch (snakeSections.getFirst().getFacing()) {
            case RIGHT:
                this.x += 1;
                break;
            case LEFT:
                this.x -= 1;
                break;
            case UP:
                this.y -= 1;
                break;
            case DOWN:
                this.y += 1;
        }

        snakeSections.getFirst().setHead(false);
        snakeSections.addFirst(new SnakeSection(true, false, moving, this.x, this.y));
        if (removeTail) {
            snakeSections.removeLast();
            snakeSections.getLast().setTail(true);
        }

    }

    public int getLength() {
        return snakeSections.size();
    }

    public SnakeSection getSection(int index) {
        if (index > snakeSections.size()) throw new IndexOutOfBoundsException();
        return new ArrayList<>(snakeSections).get(index);
    }
}