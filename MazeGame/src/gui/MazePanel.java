package gui;

import javax.swing.*;
import java.awt.*;
import model.Cell;
import model.Player;

public class MazePanel extends JPanel {
    private Cell[][] maze;
    private Player player;
    private static final int CELL_SIZE = 12; // Small cells for complex maze
    private static final int PLAYER_SIZE = 8; // Size for player and finish point

    public MazePanel(Cell[][] maze, Player player, JLabel timerLabel) {
        this.maze = maze;
        this.player = player;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw maze
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                
                if (maze[row][col].isWall()) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // Draw player
        g2d.setColor(Color.RED);
        g2d.fillOval(
            player.getX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE)/2,
            player.getY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE)/2,
            PLAYER_SIZE,
            PLAYER_SIZE
        );

        // Draw finish point
        g2d.setColor(Color.GREEN);
        g2d.fillOval(
            (maze[0].length-2) * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE)/2,
            (maze.length-2) * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE)/2,
            PLAYER_SIZE,
            PLAYER_SIZE
        );
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
            maze[0].length * CELL_SIZE,
            maze.length * CELL_SIZE
        );
    }
} 