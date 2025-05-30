package game;

import gui.GameUI;
import gui.MazePanel;
import java.awt.event.*;
import javax.swing.*;
import model.Cell;
import model.MazeGenerator;
import model.Player;

public class GameController {
    private GameState state;
    private GameUI ui;
    private Player player;
    private Cell[][] maze;
    private Timer timer;
    private MazeGenerator mazeGenerator;
    private JFrame frame;
    private long currentMazeSeed;

    public GameController(int initialRows, int initialCols, JFrame frame) {
        this.frame = frame;
        state = new GameState(initialRows, initialCols);
        initializeGame();
    }

    private void initializeGame() {
        // Initialize maze generator
        mazeGenerator = new MazeGenerator(state.getRows(), state.getCols());
        this.currentMazeSeed = mazeGenerator.getSeed();
        
        // Generate initial maze
        maze = mazeGenerator.generate();
        
        // Initialize player
        player = new Player(1, 1);
        
        // Initialize UI
        ui = new GameUI(maze, player, state);
        
        // Setup controls
        setupKeyListener();
        
        // Initialize timer
        setupTimer();
    }

    private void setupTimer() {
        timer = new Timer(1000, e -> {
            state.incrementTimer();
            ui.updateTimerLabel();
        });
        timer.start();
    }

    private void setupKeyListener() {
        ui.getMazePanel().setFocusable(true);
        ui.getMazePanel().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key Pressed: " + e.getKeyCode());
                handleKeyPress(e.getKeyCode());
            }
        });
        ui.getMazePanel().requestFocusInWindow();
    }

    private void handleKeyPress(int keyCode) {
        int newX = player.getX();
        int newY = player.getY();

        switch (keyCode) {
            case KeyEvent.VK_UP -> newY--;
            case KeyEvent.VK_DOWN -> newY++;
            case KeyEvent.VK_LEFT -> newX--;
            case KeyEvent.VK_RIGHT -> newX++;
        }

        if (isValidMove(newX, newY)) {
            player.setPosition(newX, newY);
            ui.getMazePanel().repaint();
            checkWin();
        }
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < state.getCols() && 
               y >= 0 && y < state.getRows() && 
               !maze[y][x].isWall();
    }

    private void checkWin() {
        if (player.getX() == state.getCols() - 2 && 
            player.getY() == state.getRows() - 2) {
            handleLevelComplete();
        }
    }

    private void handleLevelComplete() {
        timer.stop();
        state.updateBestTime(state.getSeconds());
        
        if (ui.showLevelCompleteDialog()) {
            startNextLevel();
        } else {
            System.exit(0);
        }
    }

    public void startNextLevel() {
        state.incrementLevel();
        
        // Generate new maze
        mazeGenerator = new MazeGenerator(state.getRows(), state.getCols());
        this.currentMazeSeed = mazeGenerator.getSeed();
        maze = mazeGenerator.generate();
        
        // Reset player position
        player.setPosition(1, 1);
        
        // Reset timer
        state.resetTimer();
        
        // Update UI
        ui.updateMazePanel(maze, player);
        setupKeyListener();
        // Restart timer
        timer.start();
    }

    public void setPlayerPosition(int x, int y) {
        player.setPosition(x, y);
        ui.getMazePanel().repaint();
    }

    public JPanel getGamePanel() {
        return ui.getMainPanel();
    }

    public MazePanel getUiMazePanel() {
        return ui.getMazePanel();
    }

    public GameUI getGameUI() {
        return ui;
    }

    public int getCurrentLevel() {
        return state.getCurrentLevel();
    }

    public int getPlayerX() {
        return player.getX();
    }

    public int getPlayerY() {
        return player.getY();
    }

    public long getCurrentMazeSeed() {
        return currentMazeSeed;
    }

    public void loadGameData(int level, int playerX, int playerY, long mazeSeed) {
        state.setCurrentLevel(level);
        state.setRows(state.getInitialRows() + (level - 1) * 2);
        state.setCols(state.getInitialCols() + (level - 1) * 2);

        mazeGenerator = new MazeGenerator(state.getRows(), state.getCols(), mazeSeed);
        this.currentMazeSeed = mazeSeed;
        maze = mazeGenerator.generate();
        
        player.setPosition(playerX, playerY);
        
        state.resetTimer();
        
        ui.updateMazePanel(maze, player);
        setupKeyListener();
        timer.start();
    }
} 