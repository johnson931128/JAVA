package gui;

import game.GameState;
import java.awt.*;
import javax.swing.*;
import model.Cell;
import model.Player;

public class GameUI {
    private JPanel mainPanel;
    private MazePanel mazePanel;
    private JLabel timerLabel;
    private final GameState gameState;

    public GameUI(Cell[][] maze, Player player, GameState gameState) {
        this.gameState = gameState;
        initializeUI(maze, player);
    }

    private void initializeUI(Cell[][] maze, Player player) {
        // Initialize timer label
        timerLabel = new JLabel();
        updateTimerLabel();
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Initialize maze panel
        mazePanel = new MazePanel(maze, player, timerLabel);
        
        // Create main panel with timer at bottom
        mainPanel = new JPanel(new BorderLayout());
        JPanel timerPanel = new JPanel();
        timerPanel.add(timerLabel);
        mainPanel.add(mazePanel, BorderLayout.CENTER);
        mainPanel.add(timerPanel, BorderLayout.SOUTH);
    }

    public void updateTimerLabel() {
        timerLabel.setText(String.format("Level: %d | Time: %d sec | Best: %s",
            gameState.getCurrentLevel(),
            gameState.getSeconds(),
            gameState.getBestTime() == Integer.MAX_VALUE ? "-" : gameState.getBestTime() + " sec"));
    }

    public void updateMazePanel(Cell[][] maze, Player player) {
        MazePanel newMazePanel = new MazePanel(maze, player, timerLabel);
        mainPanel.remove(mainPanel.getComponent(0));
        mainPanel.add(newMazePanel, BorderLayout.CENTER);
        mazePanel = newMazePanel;
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public boolean showLevelCompleteDialog() {
        int choice = JOptionPane.showConfirmDialog(mazePanel,
            String.format("Level %d Complete!\nTime: %d seconds\nBest Time: %s\n\nContinue to next level?",
                gameState.getCurrentLevel(),
                gameState.getSeconds(),
                gameState.getBestTime() == Integer.MAX_VALUE ? "-" : gameState.getBestTime() + " sec"),
            "Level Complete!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
        return choice == JOptionPane.YES_OPTION;
    }

    public MazePanel getMazePanel() { return mazePanel; }
    public JPanel getMainPanel() { return mainPanel; }
} 