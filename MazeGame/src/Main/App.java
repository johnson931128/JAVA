package Main;

import game.Continue;
import game.GameController;
import gui.LoginPanel;
import java.awt.Container;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class App {
    private static int MAZE_WIDTH;
    private static int MAZE_HEIGHT;
    private static final int FRAME_WIDTH = 1062;
    private static final int FRAME_HEIGHT = 694;

    private static void loadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/environment/config.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "MAZE_WIDTH":
                            MAZE_WIDTH = Integer.parseInt(value);
                            break;
                        case "MAZE_HEIGHT":
                            MAZE_HEIGHT = Integer.parseInt(value);
                            break;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load configuration file: " + e.getMessage());
            MAZE_WIDTH = 21; // Default value
            MAZE_HEIGHT = 21; // Default value
        }
    }

    public static int getMazeWidth() {
        return MAZE_WIDTH;
    }

    public static int getMazeHeight() {
        return MAZE_HEIGHT;
    }


    public static void main(String[] args) {
        loadConfig(); // Call loadConfig() at the start of the main method

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            System.out.println(MAZE_HEIGHT + " " + MAZE_WIDTH);
            GameController game = new GameController(MAZE_WIDTH, MAZE_HEIGHT, frame);
            JPanel gamePanel = game.getGamePanel();

            final LoginPanel[] loginPanelHolder = new LoginPanel[1];
            Runnable switchAction = () -> {
                switchPanel(frame, loginPanelHolder[0], gamePanel);
                SwingUtilities.invokeLater(() -> {
                    game.getUiMazePanel().requestFocusInWindow();
                });
            };

            loginPanelHolder[0] = new LoginPanel(switchAction);
            loginPanelHolder[0].showModeSelect();

            loginPanelHolder[0].getContinueButton().addActionListener(e -> {
                Continue.SaveData save = Continue.loadGame();
                if (save != null) {
                    game.loadGameData(save.level, save.playerX, save.playerY, save.mazeSeed);
                    switchPanel(frame, loginPanelHolder[0], gamePanel);
                    SwingUtilities.invokeLater(() -> {
                        game.getUiMazePanel().requestFocusInWindow();
                    });
                } else {
                    JOptionPane.showMessageDialog(frame, "沒有存檔可繼續，請先開始新遊戲！", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            game.getGameUI().getSaveButton().addActionListener(e -> {
                Continue.saveGame(
                        game.getCurrentLevel(),
                        game.getPlayerX(),
                        game.getPlayerY(),
                        game.getCurrentMazeSeed()
                );
                JOptionPane.showMessageDialog(frame, "存檔成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            });

            game.getGameUI().getBackToMenuButton().addActionListener(e -> {
                switchPanel(frame, gamePanel, loginPanelHolder[0]);
            });

            frame.add(loginPanelHolder[0]);
            frame.pack();
            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void switchPanel(JFrame frame, JPanel panelToRemove, JPanel panelToAdd) {
        Container contentPane = frame.getContentPane();
        contentPane.remove(panelToRemove);
        contentPane.add(panelToAdd);
        contentPane.revalidate();
        contentPane.repaint();
    }
}