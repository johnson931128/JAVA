package model;

import java.util.*;

public class MazeGenerator {
    private int rows;
    private int cols;
    private Cell[][] maze;
    private Random random;

    public MazeGenerator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.random = new Random();
        this.maze = new Cell[rows][cols];
    }

    public Cell[][] generate() {
        // Initialize all cells as walls
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = new Cell(j, i, true);
            }
        }

        // Generate maze using DFS
        generateMaze(1, 1);

        // Set entrance and exit
        maze[1][1].setWall(false);
        maze[rows-2][cols-2].setWall(false);

        return maze;
    }

    private void generateMaze(int x, int y) {
        maze[y][x].setWall(false);

        // Define directions: up, right, down, left
        int[][] directions = {{0, -2}, {2, 0}, {0, 2}, {-2, 0}};
        shuffleArray(directions);

        for (int[] dir : directions) {
            int nextX = x + dir[0];
            int nextY = y + dir[1];
            
            if (isValid(nextX, nextY) && maze[nextY][nextX].isWall()) {
                // Break the wall between cells
                maze[y + dir[1]/2][x + dir[0]/2].setWall(false);
                generateMaze(nextX, nextY);
            }
        }
    }

    private boolean isValid(int x, int y) {
        return x > 0 && x < cols-1 && y > 0 && y < rows-1;
    }

    private void shuffleArray(int[][] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
} 