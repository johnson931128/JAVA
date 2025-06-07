package game;

import java.util.HashMap;
import java.util.Map;
import model.Player;
import model.Cell;

public class GameState {
    private int currentLevel;
    private int seconds;
    private int bestTime;
    private int rows;
    private int cols;
    private int initialRows;
    private int initialCols;
    private static final int MAX_ROWS = 61;
    private static final int MAX_COLS = 91;
    private Map<Integer, Player> players = new HashMap<>();
    private Map<Integer, Integer> scores = new HashMap<>();
    private int winnerId = 0;
    private Cell[][] maze;

    public GameState(int initialRows, int initialCols) {
        this.currentLevel = 1;
        this.seconds = 0;
        this.bestTime = Integer.MAX_VALUE;
        this.rows = initialRows;
        this.cols = initialCols;
        this.initialRows = initialRows;
        this.initialCols = initialCols;
    }

    public void incrementLevel() {
        currentLevel++;
        // Increase maze size for next level
        rows = Math.min(rows + 2, MAX_ROWS);
        cols = Math.min(cols + 4, MAX_COLS);
    }

    public void updateBestTime(int newTime) {
        if (newTime < bestTime) {
            bestTime = newTime;
        }
    }

    public void resetTimer() {
        seconds = 0;
    }

    public void incrementTimer() {
        seconds++;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    // Getters
    public int getCurrentLevel() { return currentLevel; }
    public int getSeconds() { return seconds; }
    public int getBestTime() { return bestTime; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getInitialRows() { return initialRows; }
    public int getInitialCols() { return initialCols; }

    // --- Multiplayer/GameServer/RemoteGameController support ---
    public Map<Integer, Player> getPlayers() {
        return players;
    }
    public Map<Integer, Integer> getScores() {
        return scores;
    }
    public void addPoint(int playerId) {
        scores.put(playerId, scores.getOrDefault(playerId, 0) + 1);
    }
    public int getWinnerId() {
        return winnerId;
    }
    public void setWinnerId(int id) {
        winnerId = id;
    }
    public void setMaze(Cell[][] maze) {
        this.maze = maze;
    }
    public Cell[][] getMaze() {
        return maze;
    }
}