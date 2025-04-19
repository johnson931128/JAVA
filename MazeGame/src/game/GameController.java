package game;

import javax.swing.*;
import java.awt.event.*;
import model.Cell;
import model.Player;
import model.MazeGenerator;
import gui.GameUI;

public class GameController {
    private GameState state;
    private GameUI ui;
    private Player player;
    private Cell[][] maze;
    private Timer timer;
    private MazeGenerator mazeGenerator;

    public GameController(int initialRows, int initialCols) {
        state = new GameState(initialRows, initialCols);
        initializeGame();
    }

    private void initializeGame() {
        // Initialize maze generator
        mazeGenerator = new MazeGenerator(state.getRows(), state.getCols());
        
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
                handleKeyPress(e.getKeyCode());
            }
        });
        ui.getMazePanel().requestFocusInWindow();
    }

    private void handleKeyPress(int keyCode) {
        int newX = player.getX();
        int newY = player.getY();

        switch (keyCode) {
            case KeyEvent.VK_UP:    newY--; break;
            case KeyEvent.VK_DOWN:  newY++; break;
            case KeyEvent.VK_LEFT:  newX--; break;
            case KeyEvent.VK_RIGHT: newX++; break;
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

    private void startNextLevel() {
        state.incrementLevel();
        
        // Generate new maze
        mazeGenerator = new MazeGenerator(state.getRows(), state.getCols());
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

    public JPanel getGamePanel() {
        return ui.getMainPanel();
    }
} 