package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.Cell;
import model.Player;
import model.MazeGenerator;
import gui.MazePanel;

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
        
        // Initialize panel
        mazePanel = new MazePanel(maze, player, timerLabel);
        
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
        
        // Create new maze panel
        MazePanel newMazePanel = new MazePanel(maze, player, timerLabel);
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
        mazePanel.setFocusable(true);
        mazePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                movePlayer(e.getKeyCode());
            }
        });
        mazePanel.requestFocusInWindow();
    }

    private void movePlayer(int keyCode) {
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