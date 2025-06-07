package game;

import gui.MazePanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.Cell;
import model.MazeGenerator;
import model.Player;
import java.util.HashMap;
import java.util.Map;

public class MazeGame {
    private MazePanel mazePanel;
    private Player player;
    private Cell[][] maze;
    private int rows;
    private int cols;
    private Timer timer;
    private int seconds;
    private JLabel timerLabel;
    private JPanel mainPanel;
    private int currentLevel = 1;
    private int bestTime = Integer.MAX_VALUE;

    public MazeGame(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        initializeGame();
    }

    private void initializeGame() {
        // Initialize maze
        maze = new MazeGenerator(rows, cols).generate();
        
        // Initialize player
        player = new Player(1, 1);
        
        // Initialize timer label
        timerLabel = new JLabel("Level: " + currentLevel + " | Time: 0 sec | Best: " + 
            (bestTime == Integer.MAX_VALUE ? "-" : bestTime + " sec"));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // 包裝成 Map<Integer, Player>
        Map<Integer, Player> singlePlayerMap = new HashMap<>();
        singlePlayerMap.put(1, player);
        
        // Initialize panel
        mazePanel = new MazePanel(maze, singlePlayerMap, timerLabel);
        
        // Create main panel with timer at bottom
        mainPanel = new JPanel(new BorderLayout());
        JPanel timerPanel = new JPanel();
        timerPanel.add(timerLabel);
        mainPanel.add(mazePanel, BorderLayout.CENTER);
        mainPanel.add(timerPanel, BorderLayout.SOUTH);
        
        // Setup keyboard listener
        setupKeyListener();
        
        // Initialize timer
        setupTimer();
    }

    private void resetGame() {
        // Update best time if current time is better
        if (seconds < bestTime) {
            bestTime = seconds;
        }
        
        // Increment level
        currentLevel++;
        
        // Stop current timer
        timer.stop();
        
        // Generate new maze (make it slightly bigger for each level)
        rows = Math.min(rows + 2, 61);
        cols = Math.min(cols + 4, 91);
        
        // Reset player position
        player.setPosition(1, 1);
        
        // Generate new maze
        maze = new MazeGenerator(rows, cols).generate();
        
        // Reset timer
        seconds = 0;
        timerLabel.setText("Level: " + currentLevel + " | Time: 0 sec | Best: " + 
            (bestTime == Integer.MAX_VALUE ? "-" : bestTime + " sec"));
        
        // 包裝成 Map<Integer, Player>
        Map<Integer, Player> singlePlayerMap = new HashMap<>();
        singlePlayerMap.put(1, player);
        
        // Create new maze panel
        MazePanel newMazePanel = new MazePanel(maze, singlePlayerMap, timerLabel);
        mainPanel.remove(mainPanel.getComponent(0)); // Remove old maze panel
        mainPanel.add(newMazePanel, BorderLayout.CENTER);
        mazePanel = newMazePanel;
        
        // Setup keyboard listener for new panel
        setupKeyListener();
        
        // Start timer
        timer.start();
        
        // Request focus and refresh display
        mazePanel.requestFocusInWindow();
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void setupTimer() {
        seconds = 0;
        timer = new Timer(1000, e -> {
            seconds++;
            timerLabel.setText("Level: " + currentLevel + " | Time: " + seconds + " sec | Best: " + 
                (bestTime == Integer.MAX_VALUE ? "-" : bestTime + " sec"));
        });
        timer.start();
    }

    private void setupKeyListener() {
        // 先移除所有舊的 keyListener，避免重複
        for (KeyListener kl : mazePanel.getKeyListeners()) {
            mazePanel.removeKeyListener(kl);
        }
        mazePanel.setFocusable(true);
        mazePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("[DEBUG] keyPressed: " + e.getKeyCode());
                movePlayer(e.getKeyCode());
            }
        });
        mazePanel.requestFocusInWindow();
    }

    private void movePlayer(int keyCode) {
        int currentX = player.getX(); // Use different names to avoid conflict in lambda
        int currentY = player.getY();

        // Use enhanced switch (rule switch)
        int newX = switch (keyCode) {
            case KeyEvent.VK_LEFT -> currentX - 1;
            case KeyEvent.VK_RIGHT -> currentX + 1;
            default -> currentX; // Default case for X coordinate
        };
        int newY = switch (keyCode) {
            case KeyEvent.VK_UP -> currentY - 1;
            case KeyEvent.VK_DOWN -> currentY + 1;
            default -> currentY; // Default case for Y coordinate
        };

        if (isValidMove(newX, newY)) {
            player.setPosition(newX, newY);
            mazePanel.repaint();
            checkWin();
        }
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows && !maze[y][x].isWall();
    }

    private void checkWin() {
        if (player.getX() == cols - 2 && player.getY() == rows - 2) {
            timer.stop();
            int choice = JOptionPane.showConfirmDialog(mazePanel,
                "Level " + currentLevel + " Complete!\n" +
                "Time: " + seconds + " seconds\n" +
                "Best Time: " + (bestTime == Integer.MAX_VALUE ? "-" : bestTime + " sec") + "\n\n" +
                "Continue to next level?",
                "Level Complete!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
                
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

    public JPanel getMazePanel() {
        return mainPanel;
    }
}