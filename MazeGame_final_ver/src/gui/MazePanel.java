package gui;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.Cell;
import model.Player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

public class MazePanel extends JPanel {
    private BufferedImage backgroundImage;
    private final Cell[][] maze;
    private Map<Integer, Player> players;
    private static final int CELL_SIZE = 12; // Small cells for complex maze
    private static final int PLAYER_SIZE = 8; // Size for player and finish point

    public MazePanel(Cell[][] maze, Map<Integer, Player> players, JLabel timerLabel) {
        this.maze = maze;
        this.players = players;
        setBackground(Color.WHITE);
        setFocusable(true); // 確保可以取得鍵盤焦點
        setPreferredSize(new Dimension(
            Math.max(maze[0].length * CELL_SIZE, 900),
            Math.max(maze.length * CELL_SIZE, 600)
        )); // 最小900x600

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/image/background.png"));
        } catch (IOException e) {
            System.err.println("無法載入背景圖片: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        Graphics2D g2d = (Graphics2D) g.create();
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

        // Draw all players
        if (players != null) {
            // 依 playerId 指定顏色，1=紅, 2=藍, 3=紫, 4=橘, 5=青, 6=粉
            Color[] playerColors = {Color.RED, Color.BLUE, Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.PINK};
            for (Map.Entry<Integer, Player> entry : players.entrySet()) {
                Player p = entry.getValue();
                int id = entry.getKey();
                Color c = playerColors[(id-1) % playerColors.length];
                g2d.setColor(c);
                g2d.fillOval(
                    p.getX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE)/2,
                    p.getY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE)/2,
                    PLAYER_SIZE,
                    PLAYER_SIZE
                );
            }
        }

        // Draw finish point
        g2d.setColor(Color.GREEN);
        g2d.fillOval(
            (maze[0].length-2) * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE)/2,
            (maze.length-2) * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE)/2,
            PLAYER_SIZE,
            PLAYER_SIZE
        );
    }
    
    public void setPlayers(Map<Integer,Player> players){
        this.players = players;
    }
    public Cell[][] getMaze(){           // GameUI 要判斷是否真的換圖
        return maze;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
            maze[0].length * CELL_SIZE,
            maze.length * CELL_SIZE
        );
    }
}