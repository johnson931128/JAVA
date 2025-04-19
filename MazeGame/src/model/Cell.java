package model;

public class Cell {
    private boolean isWall;
    private int x;
    private int y;

    public Cell(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
} 